package com.pedroid.qrcodecomposelib.scan.internal

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.pedroid.qrcodecomposelib.scan.QRCodeCameraAnalyzer
import com.pedroid.qrcodecomposelib.scan.QRCodeFileAnalyzer
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import java.io.FileNotFoundException
import java.nio.ByteBuffer

private val supportedImageFormats: List<Int> by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        listOf(
            ImageFormat.YUV_420_888,
            ImageFormat.YUV_422_888,
            ImageFormat.YUV_444_888,
        )
    } else {
        listOf(
            ImageFormat.YUV_420_888,
        )
    }
}

private val qrCodeReader: Reader by lazy {
    MultiFormatReader().apply {
        setHints(mapOf(DecodeHintType.POSSIBLE_FORMATS to arrayListOf(BarcodeFormat.QR_CODE)))
    }
}

private const val LOG_TAG = "ZxingAnalyzer"

internal class ZxingAnalyzer(
    override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
) : QRCodeCameraAnalyzer, QRCodeFileAnalyzer {
    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormats) {
            val binaryBmp = image.convertToBinaryBitmap()
            try {
                val result = qrCodeReader.decode(binaryBmp)
                Log.d(LOG_TAG, "Scanned $result")
                onQRCodeStatus(QRCodeScanResult.Scanned(result.text))
            } catch (nfe: NotFoundException) {
                Log.v(
                    LOG_TAG,
                    "Obtained error decoding image for qr code, " +
                        "most likely this means there was no qr code in image " +
                        "(hence why it was not found",
                    nfe,
                )
                onQRCodeStatus(QRCodeScanResult.Invalid)
            } finally {
                image.close()
            }
        } else {
            Log.d(
                LOG_TAG,
                "Got ${image.format}, but it's not on supported image list: $onQRCodeStatus",
            )
            onQRCodeStatus(QRCodeScanResult.Invalid)
        }
    }

    override fun analyze(
        context: Context,
        uri: Uri,
    ) {
        val scannedCode: String? =
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val binaryBitmap = BitmapFactory.decodeStream(inputStream).convertToBinaryBitmap()
                    try {
                        qrCodeReader.decode(binaryBitmap)?.text
                    } catch (nfe: NotFoundException) {
                        Log.d(LOG_TAG, "No QR Code was found in image", nfe)
                        null
                    }
                }
            } catch (nfe: FileNotFoundException) {
                Log.d(LOG_TAG, "QR Code image file not found", nfe)
                null
            }
        onQRCodeStatus(
            if (scannedCode != null) QRCodeScanResult.Scanned(scannedCode) else QRCodeScanResult.Invalid,
        )
    }

    private fun ImageProxy.convertToBinaryBitmap(): BinaryBitmap {
        val bytes = planes.first().buffer.toByteArray()
        val source =
            PlanarYUVLuminanceSource(
                bytes,
                width,
                height,
                0,
                0,
                width,
                height,
                false,
            )
        return BinaryBitmap(HybridBinarizer(source))
    }

    private fun Bitmap.convertToBinaryBitmap(): BinaryBitmap {
        val source = RGBLuminanceSource(width, height, toByteArray())
        recycle()
        return BinaryBitmap(HybridBinarizer(source))
    }

    private fun Bitmap.toByteArray(): IntArray =
        IntArray(size = width * height).also {
            this.getPixels(it, 0, width, 0, 0, width, height)
        }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also {
            get(it)
        }
    }
}
