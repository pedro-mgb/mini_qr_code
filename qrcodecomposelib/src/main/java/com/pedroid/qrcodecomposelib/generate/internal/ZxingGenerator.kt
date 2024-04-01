package com.pedroid.qrcodecomposelib.generate.internal

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

private const val LOG_TAG = "ZxingGenerator"

/**
 * Returns the generated QR Code (or other barcode format) bitmap, error result if it fails
 */
internal fun generateCodeViaZxing(
    text: String,
    width: Int,
    aspectRatio: Float,
    format: BarcodeFormat = BarcodeFormat.QR_CODE,
    @ColorInt colorFill: Int = DEFAULT_FILL_COLOR,
    encoding: String = DEFAULT_QR_CODE_TEXT_ENCODING,
): QRCodeGenerateResult {
    Log.d(LOG_TAG, "Generating $text; width=$width, aspectRatio=$aspectRatio, format=$format, colorFill=$colorFill, encoding=$encoding")
    return try {
        val multiFormatWriter = MultiFormatWriter()
        val height = ((width / aspectRatio).toInt())
        val encodingHints: Map<EncodeHintType, Any> =
            mutableMapOf<EncodeHintType, Any>(EncodeHintType.CHARACTER_SET to encoding).also { map ->
                getErrorCorrectionLevel(format)?.let {
                    map[EncodeHintType.ERROR_CORRECTION] = it
                }
            }
        Log.d(LOG_TAG, "Generating qrcode: $text; format=$format; width=$width; height=$height")
        val bitMatrix: BitMatrix =
            multiFormatWriter.encode(
                text,
                format,
                width,
                height,
                encodingHints,
            )
        createQRCodeBitmap(bitMatrix, colorFill).let {
            QRCodeGenerateResult.Generated(it)
        }
    } catch (wEx: WriterException) {
        wEx.printStackTrace()
        QRCodeGenerateResult.Error(wEx)
    } catch (iEx: IllegalArgumentException) {
        iEx.printStackTrace()
        QRCodeGenerateResult.Error(iEx)
    } catch (iEx: ArrayIndexOutOfBoundsException) {
        if (format == BarcodeFormat.CODE_93) {
            // this is a workaround because if there's some wrong formatting with CODE_93,
            // an IllegalArgumentException is not thrown
            iEx.printStackTrace()
            QRCodeGenerateResult.Error(IllegalArgumentException(iEx))
        } else {
            throw iEx
        }
    }
}

/**
 * get error correction level for barcode generation with ZXING.
 * Error correction level defines level of replication of data in the barcode.
 * The higher data is replicated, the increased changes of code being read even if partially corrupted.
 *
 * @param format the ZXING barcode format. Currently only supported for QR Code
 *
 * @return the error correction level, or null if no value is specified for the format
 */
private fun getErrorCorrectionLevel(format: BarcodeFormat): Any? {
    return when (format) {
        BarcodeFormat.QR_CODE -> ErrorCorrectionLevel.H
        else -> null
    }
}

private fun createQRCodeBitmap(
    matrix: BitMatrix,
    @ColorInt colorFill: Int,
): Bitmap {
    val width = matrix.width
    val height = matrix.height
    val pixels = IntArray(width * height) { DEFAULT_BLANK_COLOR }
    for (y: Int in 0 until height) {
        val offset = y * width
        for (x: Int in 0 until width) {
            if (matrix.get(x, y)) {
                pixels[offset + x] = colorFill
            }
        }
    }
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
    return bitmap
}
