package com.pedroid.qrcodecomposelibmlkit.internal

import com.google.mlkit.vision.barcode.common.Barcode
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

/**
 * @returns the actual Format from [com.google.mlkit.vision.barcode.common.Barcode] format int, or null if in an unknown format
 */
internal fun Int.toQRCodeComposeXFormat(): QRCodeComposeXFormat? {
    return when (this) {
        Barcode.FORMAT_QR_CODE -> QRCodeComposeXFormat.QR_CODE
        Barcode.FORMAT_AZTEC -> QRCodeComposeXFormat.AZTEC
        Barcode.FORMAT_DATA_MATRIX -> QRCodeComposeXFormat.DATA_MATRIX
        Barcode.FORMAT_PDF417 -> QRCodeComposeXFormat.PDF_417
        Barcode.FORMAT_CODABAR -> QRCodeComposeXFormat.CODABAR
        Barcode.FORMAT_CODE_39 -> QRCodeComposeXFormat.BARCODE_39
        Barcode.FORMAT_CODE_93 -> QRCodeComposeXFormat.BARCODE_93
        Barcode.FORMAT_CODE_128 -> QRCodeComposeXFormat.BARCODE_128
        Barcode.FORMAT_EAN_8 -> QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8
        Barcode.FORMAT_EAN_13 -> QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13
        Barcode.FORMAT_ITF -> QRCodeComposeXFormat.BARCODE_ITF
        Barcode.FORMAT_UPC_A -> QRCodeComposeXFormat.BARCODE_US_UPC_A
        Barcode.FORMAT_UPC_E -> QRCodeComposeXFormat.BARCODE_US_UPC_E
        else -> null // Unknown format
    }
}

/**
 * @returns the [com.google.mlkit.vision.barcode.common.Barcode] format int from this lib's QRCodeComposeXFormat
 */
internal fun QRCodeComposeXFormat.toMLKitBarcodeFormat(): Int {
    return when (this) {
        QRCodeComposeXFormat.QR_CODE -> Barcode.FORMAT_QR_CODE
        QRCodeComposeXFormat.AZTEC -> Barcode.FORMAT_AZTEC
        QRCodeComposeXFormat.DATA_MATRIX -> Barcode.FORMAT_DATA_MATRIX
        QRCodeComposeXFormat.PDF_417 -> Barcode.FORMAT_PDF417
        QRCodeComposeXFormat.CODABAR -> Barcode.FORMAT_CODABAR
        QRCodeComposeXFormat.BARCODE_39 -> Barcode.FORMAT_CODE_39
        QRCodeComposeXFormat.BARCODE_93 -> Barcode.FORMAT_CODE_93
        QRCodeComposeXFormat.BARCODE_128 -> Barcode.FORMAT_CODE_128
        QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8 -> Barcode.FORMAT_EAN_8
        QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13 -> Barcode.FORMAT_EAN_13
        QRCodeComposeXFormat.BARCODE_ITF -> Barcode.FORMAT_ITF
        QRCodeComposeXFormat.BARCODE_US_UPC_A -> Barcode.FORMAT_UPC_A
        QRCodeComposeXFormat.BARCODE_US_UPC_E -> Barcode.FORMAT_UPC_E
    }
}
