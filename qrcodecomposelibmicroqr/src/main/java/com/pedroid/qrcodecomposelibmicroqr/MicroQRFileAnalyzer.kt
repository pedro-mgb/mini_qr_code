package com.pedroid.qrcodecomposelibmicroqr

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import com.pedroid.qrcodecomposelib.scan.QRCodeFileAnalyzer
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import zxingcpp.BarcodeReader
import java.io.IOException
import java.util.concurrent.RejectedExecutionException
import kotlin.math.max

private const val LOG_TAG = "MicroQRFileAnalyzer"

/** Images with a longer side than this (in pixels) are scaled down before being analyzed. */
internal const val MAX_IMAGE_SIDE = 3072

/**
 * File analyzer that only detects Micro QR Codes, using zxing-cpp. Meant to complement an analyzer
 * that can't read them (e.g. ML Kit), not to replace it.
 *
 * Unlike [MicroQRCameraAnalyzer] this is done once for a single image, so there is no need to be economical:
 * all the zxing-cpp "try harder" options are on (also reads rotated and inverted codes, and tries smaller versions of the image).
 *
 * - Decoding is done on its own thread, and [onQRCodeStatus] is always called on the main thread.
 * - Images are scaled down if too large (see [MAX_IMAGE_SIDE]), to not run out of memory with photos from high resolution cameras.
 * - The image's EXIF orientation is taken into account, as that is not done when decoding the image.
 *
 * Anything that goes wrong (e.g. file can't be read) is reported as [QRCodeScanResult.Invalid]. This is because it's
 * meant to be run after another analyzer has already reported that, and any real error was reported by that one.
 *
 * Micro QR Codes are reported with the QR Code format, as it has no format of its own (yet).
 *
 * @param onQRCodeStatus listener for the result of the analysis
 */
class MicroQRFileAnalyzer(
    override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
) : QRCodeFileAnalyzer {
    // lazy so the native library is loaded on the decoding thread
    private val reader by lazy {
        BarcodeReader(
            BarcodeReader.Options(
                formats = setOf(BarcodeReader.Format.MICRO_QR_CODE),
                tryHarder = true,
                tryRotate = true,
                tryInvert = true,
                tryDownscale = true,
            ),
        )
    }
    private val decodeExecutor = newDecodeExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun analyze(
        context: Context,
        uri: Uri,
    ) {
        // only the resolver goes to the other thread, so a screen is not kept alive by it
        val contentResolver = context.contentResolver
        try {
            decodeExecutor.execute { decode(contentResolver, uri) }
        } catch (e: RejectedExecutionException) {
            Log.w(LOG_TAG, "Could not schedule decoding", e)
            postStatus(QRCodeScanResult.Invalid)
        }
    }

    private fun decode(
        contentResolver: ContentResolver,
        uri: Uri,
    ) {
        val status =
            try {
                scan(contentResolver, uri)
            } catch (e: IOException) {
                Log.w(LOG_TAG, "Could not read image file $uri", e)
                QRCodeScanResult.Invalid
            } catch (e: RuntimeException) {
                // e.g. no permission to read the file, or a native error
                Log.w(LOG_TAG, "Obtained error processing image for micro qr code", e)
                QRCodeScanResult.Invalid
            } catch (e: OutOfMemoryError) {
                Log.w(LOG_TAG, "Image $uri is too large to be analyzed", e)
                QRCodeScanResult.Invalid
            }
        postStatus(status)
    }

    private fun scan(
        contentResolver: ContentResolver,
        uri: Uri,
    ): QRCodeScanResult {
        val bitmap = contentResolver.decodeScaledBitmap(uri) ?: return QRCodeScanResult.Invalid
        return try {
            reader
                .read(bitmap, rotation = contentResolver.readRotationDegrees(uri))
                .firstOrNull()
                ?.toQRCodeScanResult()
                ?: QRCodeScanResult.Invalid
        } finally {
            bitmap.recycle()
        }
    }

    private fun postStatus(status: QRCodeScanResult) {
        mainHandler.post { onQRCodeStatus(status) }
    }
}

/**
 * @return the power of 2 to scale an image of the given size down by (as that's what the image decoder works best with),
 *      so that its longer side is at most [maxSide]. 1 if it's already small enough
 */
internal fun calculateSampleSize(
    width: Int,
    height: Int,
    maxSide: Int = MAX_IMAGE_SIDE,
): Int {
    var sampleSize = 1
    while (max(width, height) / sampleSize > maxSide) {
        sampleSize *= 2
    }
    return sampleSize
}

private fun ContentResolver.decodeScaledBitmap(uri: Uri): Bitmap? {
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
    if (bounds.outWidth <= 0 || bounds.outHeight <= 0) {
        return null
    }
    val options =
        BitmapFactory.Options().apply {
            inSampleSize = calculateSampleSize(bounds.outWidth, bounds.outHeight)
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
    val bitmap = openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
    // zxing-cpp only reads 8 bit per channel bitmaps, and some images (e.g. wide gamut) may be decoded into a different config
    return if (bitmap != null && bitmap.config != Bitmap.Config.ARGB_8888) {
        bitmap.copy(Bitmap.Config.ARGB_8888, false).also { bitmap.recycle() }
    } else {
        bitmap
    }
}

/**
 * @return how many degrees (clockwise) the image has to be rotated to be upright, according to its EXIF data. 0 if unknown
 */
private fun ContentResolver.readRotationDegrees(uri: Uri): Int =
    try {
        openInputStream(uri)?.use { ExifInterface(it).rotationDegrees } ?: 0
    } catch (e: IOException) {
        Log.d(LOG_TAG, "Could not read orientation of $uri", e)
        0
    }
