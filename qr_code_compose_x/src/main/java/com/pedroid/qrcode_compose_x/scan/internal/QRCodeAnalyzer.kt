package com.pedroid.qrcode_compose_x.scan.internal

import android.graphics.ImageFormat
import android.os.Build
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.Reader
import com.google.zxing.common.HybridBinarizer
import com.pedroid.qrcode_compose_x.scan.QRCodeScanResult
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

private const val LOG_TAG = "QrCodeAnalyzer"

internal class QrCodeAnalyzer(
    private val onQrCodeStatus: (QRCodeScanResult) -> Unit,
) : ImageAnalysis.Analyzer {
    override fun analyze(image: ImageProxy) {
        if (image.format in supportedImageFormats) {
            val binaryBmp = image.convertToBitmap()
            try {
                val result = qrCodeReader.decode(binaryBmp)
                Log.d(LOG_TAG, "Scanned $result")
                onQrCodeStatus(QRCodeScanResult.Scanned(result.text))
            } catch (nfe: NotFoundException) {
                Log.v(
                    LOG_TAG,
                    "Obtained error decoding image for qr code, most likely this means there was no qr code in image (hence why it was not found",
                    nfe,
                )
                onQrCodeStatus(QRCodeScanResult.Invalid)
            } finally {
                image.close()
            }
        } else {
            Log.d(LOG_TAG, "Got ${image.format}, but it's not on supported image list: $onQrCodeStatus")
            onQrCodeStatus(QRCodeScanResult.Invalid)
        }
    }

    private fun ImageProxy.convertToBitmap(): BinaryBitmap {
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

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        return ByteArray(remaining()).also {
            get(it)
        }
    }
}
