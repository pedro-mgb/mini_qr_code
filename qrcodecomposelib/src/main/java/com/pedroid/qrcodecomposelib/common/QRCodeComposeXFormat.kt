package com.pedroid.qrcodecomposelib.common

/**
 * Enum class with all the possible Formats that should be supported by QRCodeComposeX Scanning and Generation
 *
 * Based off of Zxings list (missing a few formats)
 *
 * @property dimensions the number of dimensions of the format (2 if 2D, 1 if 1D)
 * @property preferredAspectRatio the aspect ratio of the format - width divided by height - 1 if it's a square
 *      Note that some formats do not have a strict value for aspect ratio (not the case for QR Code, but it's the case for e.g. PDF_417),
 *      so this is more of a preferred or average value for aspect ratio
 *      If you experience issues with stretched out or compressed, open an issue on github describing what kind of barcode did you use
 */
enum class QRCodeComposeXFormat(
    val dimensions: Int,
    val preferredAspectRatio: Float,
) {
    /** QR Code 2D barcode format. Should be the default format */
    QR_CODE(
        dimensions = 2,
        preferredAspectRatio = 1f,
    ),

    /** Aztec 2D barcode format. */
    AZTEC(
        dimensions = 2,
        preferredAspectRatio = 1f,
    ),

    /** Data Matrix 2D barcode format. */
    DATA_MATRIX(
        dimensions = 2,
        preferredAspectRatio = 1f,
    ),

    /** PDF417 2D format. */
    PDF_417(
        dimensions = 2,
        preferredAspectRatio = 3f,
    ),

    /** Code 39 1D format. */
    BARCODE_39(
        dimensions = 1,
        preferredAspectRatio = 2.5f,
    ),

    /** Code 93 1D format. */
    BARCODE_93(
        dimensions = 1,
        preferredAspectRatio = 2.5f,
    ),

    /** Code 128 1D format. */
    BARCODE_128(
        dimensions = 1,
        preferredAspectRatio = 2.5f,
    ),

    /** EAN-8 1D format. */
    BARCODE_EUROPE_EAN_8(
        dimensions = 1,
        preferredAspectRatio = 1.2f,
    ),

    /** EAN-13 1D format. */
    BARCODE_EUROPE_EAN_13(
        dimensions = 1,
        preferredAspectRatio = 1.285f,
    ),

    /** ITF (Interleaved Two of Five) 1D format. AKA ITF-14 */
    BARCODE_ITF(
        dimensions = 1,
        preferredAspectRatio = 3.66f,
    ),

    /** UPC-A 1D format. */
    BARCODE_US_UPC_A(
        dimensions = 1,
        preferredAspectRatio = 1.5f,
    ),

    /** UPC-E 1D format. */
    BARCODE_US_UPC_E(
        dimensions = 1,
        preferredAspectRatio = 1.5f,
    ),

    /** CODABAR 1D format. */
    CODABAR(
        dimensions = 1,
        preferredAspectRatio = 2.5f,
    ),
}
