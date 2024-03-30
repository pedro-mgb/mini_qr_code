package com.pedroid.qrcodecomposelib.generate.internal

import android.graphics.Bitmap
import androidx.annotation.ColorInt
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

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
    return try {
        val multiFormatWriter = MultiFormatWriter()
        val height = ((width / aspectRatio).toInt())
        val encodingHints: Map<EncodeHintType, Any> =
            mutableMapOf<EncodeHintType, Any>(EncodeHintType.CHARACTER_SET to encoding).also { map ->
                getErrorCorrectionLevel(format)?.let {
                    map[EncodeHintType.ERROR_CORRECTION] = it
                }
            }
        val bitMatrix: BitMatrix =
            multiFormatWriter.encode(
                text,
                format,
                width,
                height,
                encodingHints,
            )
        createQRCodeBitmap(bitMatrix, width, height, colorFill).let {
            QRCodeGenerateResult.Generated(it)
        }
    } catch (wEx: WriterException) {
        wEx.printStackTrace()
        QRCodeGenerateResult.Error(wEx)
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
    width: Int,
    height: Int,
    @ColorInt colorFill: Int,
): Bitmap {
    val pixels: IntArray =
        List(matrix.width * matrix.height) {
            if (matrix.get(it % matrix.width, it / matrix.height)) colorFill else DEFAULT_BLANK_COLOR
        }.toIntArray()
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bitmap.setPixels(pixels, 0, width, 0, 0, matrix.width, matrix.height)
    return bitmap
}
