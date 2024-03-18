package com.pedroid.qrcodecomposelib.common.internal

import com.google.zxing.BarcodeFormat
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

/**
 * @returns the actual Format from [com.google.zxing.BarcodeFormat], or null if in an unknown format
 */
internal fun BarcodeFormat.toQRCodeComposeXFormat(): QRCodeComposeXFormat? {
    return when (this) {
        BarcodeFormat.QR_CODE -> QRCodeComposeXFormat.QR_CODE
        BarcodeFormat.AZTEC -> QRCodeComposeXFormat.AZTEC
        BarcodeFormat.DATA_MATRIX -> QRCodeComposeXFormat.DATA_MATRIX
        BarcodeFormat.PDF_417 -> QRCodeComposeXFormat.PDF_417
        BarcodeFormat.CODABAR -> QRCodeComposeXFormat.CODABAR
        BarcodeFormat.CODE_39 -> QRCodeComposeXFormat.BARCODE_39
        BarcodeFormat.CODE_93 -> QRCodeComposeXFormat.BARCODE_93
        BarcodeFormat.CODE_128 -> QRCodeComposeXFormat.BARCODE_128
        BarcodeFormat.EAN_8 -> QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8
        BarcodeFormat.EAN_13 -> QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13
        BarcodeFormat.ITF -> QRCodeComposeXFormat.BARCODE_ITF
        BarcodeFormat.UPC_A -> QRCodeComposeXFormat.BARCODE_US_UPC_A
        BarcodeFormat.UPC_E -> QRCodeComposeXFormat.BARCODE_US_UPC_E
        else -> null // Unknown format
    }
}

/**
 * @returns the [com.google.zxing.BarcodeFormat] from this lib's QRCodeComposeXFormat
 */
internal fun QRCodeComposeXFormat.toZxingBarcodeFormat(): BarcodeFormat {
    return when (this) {
        QRCodeComposeXFormat.QR_CODE -> BarcodeFormat.QR_CODE
        QRCodeComposeXFormat.AZTEC -> BarcodeFormat.AZTEC
        QRCodeComposeXFormat.DATA_MATRIX -> BarcodeFormat.DATA_MATRIX
        QRCodeComposeXFormat.PDF_417 -> BarcodeFormat.PDF_417
        QRCodeComposeXFormat.CODABAR -> BarcodeFormat.CODABAR
        QRCodeComposeXFormat.BARCODE_39 -> BarcodeFormat.CODE_39
        QRCodeComposeXFormat.BARCODE_93 -> BarcodeFormat.CODE_93
        QRCodeComposeXFormat.BARCODE_128 -> BarcodeFormat.CODE_128
        QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8 -> BarcodeFormat.EAN_8
        QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13 -> BarcodeFormat.EAN_13
        QRCodeComposeXFormat.BARCODE_ITF -> BarcodeFormat.ITF
        QRCodeComposeXFormat.BARCODE_US_UPC_A -> BarcodeFormat.UPC_A
        QRCodeComposeXFormat.BARCODE_US_UPC_E -> BarcodeFormat.UPC_E
    }
}
