package com.pedroid.qrcodecomposelib.common

/**
 * Enum class with all the possible Formats that should be supported by QRCodeComposeX Scanning and Generation
 *
 * Based off of Zxings list
 */
enum class QRCodeComposeXFormat {
    /** QR Code 2D barcode format. Should be the default format */
    QR_CODE,

    /** Aztec 2D barcode format. */
    AZTEC,

    /** Data Matrix 2D barcode format. */
    DATA_MATRIX,

    /** PDF417 2D format. */
    PDF_417,

    /** Code 39 1D format. */
    BARCODE_39,

    /** Code 93 1D format. */
    BARCODE_93,

    /** Code 128 1D format. */
    BARCODE_128,

    /** EAN-8 1D format. */
    BARCODE_EUROPE_EAN_8,

    /** EAN-13 1D format. */
    BARCODE_EUROPE_EAN_13,

    /** ITF (Interleaved Two of Five) 1D format. */
    BARCODE_ITF,

    /** UPC-A 1D format. */
    BARCODE_US_UPC_A,

    /** UPC-E 1D format. */
    BARCODE_US_UPC_E,

    /** CODABAR 1D format. */
    CODABAR,
}

val QRCodeComposeXFormat.dimensions: Int
    get() =
        when (this) {
            QRCodeComposeXFormat.QR_CODE,
            QRCodeComposeXFormat.AZTEC,
            QRCodeComposeXFormat.DATA_MATRIX,
            QRCodeComposeXFormat.PDF_417,
            -> 2

            else -> 1
        }

val QRCodeComposeXFormat.squareFormat: Boolean
    get() =
        when (this) {
            QRCodeComposeXFormat.QR_CODE,
            QRCodeComposeXFormat.AZTEC,
            QRCodeComposeXFormat.DATA_MATRIX,
            -> true

            else -> false
        }
