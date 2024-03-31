package com.pedroid.qrcodecomposelib.common

import androidx.annotation.StringRes
import com.pedroid.qrcodecomposelib.R

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
 * @property validationRegex regex to validate if content complies with the format standard;
 *      or null if there's no standard or limitations in the content
 * @property maxLength maximum length of the content for the format
 * @property titleStringId Unique ID for string resource for title of the format
 * @property subtitleStringId Unique ID for string resource for subtitle containing short description of the format
 */
enum class QRCodeComposeXFormat(
    val dimensions: Int,
    val preferredAspectRatio: Float,
    // TODO supply additional values and perhaps documentation
    //  or have a separate validator class
    val validationRegex: Regex? = null,
    val maxLength: Int = Int.MAX_VALUE,
    @StringRes val titleStringId: Int,
    @StringRes val subtitleStringId: Int,
) {
    /** QR Code 2D barcode format. Should be the default format */
    QR_CODE(
        dimensions = 2,
        preferredAspectRatio = 1f,
        titleStringId = R.string.qrcode_compose_lib_format_qrcode_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_qrcode_subtitle,
    ),

    /** Aztec 2D barcode format. */
    AZTEC(
        dimensions = 2,
        preferredAspectRatio = 1f,
        titleStringId = R.string.qrcode_compose_lib_format_aztec_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_aztec_subtitle,
    ),

    /** Data Matrix 2D barcode format. */
    DATA_MATRIX(
        dimensions = 2,
        preferredAspectRatio = 1f,
        titleStringId = R.string.qrcode_compose_lib_format_data_matrix_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_data_matrix_subtitle,
    ),

    /** PDF417 2D format. */
    PDF_417(
        dimensions = 2,
        preferredAspectRatio = 3f,
        titleStringId = R.string.qrcode_compose_lib_format_pdf_417_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_pdf417_subtitle,
    ),

    /** Code 39 1D format. */
    BARCODE_39(
        dimensions = 1,
        preferredAspectRatio = 2f,
        maxLength = 80,
        titleStringId = R.string.qrcode_compose_lib_format_barcode39_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_barcode39_subtitle,
    ),

    /** Code 93 1D format. */
    BARCODE_93(
        dimensions = 1,
        preferredAspectRatio = 2f,
        maxLength = 80,
        titleStringId = R.string.qrcode_compose_lib_format_barcode93_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_barcode93_subtitle,
    ),

    /** Code 128 1D format. */
    BARCODE_128(
        dimensions = 1,
        preferredAspectRatio = 2f,
        maxLength = 80,
        titleStringId = R.string.qrcode_compose_lib_format_barcode128_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_barcode128_subtitle,
    ),

    /** EAN-8 1D format. */
    BARCODE_EUROPE_EAN_8(
        dimensions = 1,
        preferredAspectRatio = 1.2f,
        maxLength = 8,
        titleStringId = R.string.qrcode_compose_lib_format_ean8_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_ean8_subtitle,
    ),

    /** EAN-13 1D format. */
    BARCODE_EUROPE_EAN_13(
        dimensions = 1,
        preferredAspectRatio = 1.285f,
        maxLength = 13,
        titleStringId = R.string.qrcode_compose_lib_format_ean13_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_ean13_subtitle,
    ),

    /** ITF (Interleaved Two of Five) 1D format. AKA ITF-14 */
    BARCODE_ITF(
        dimensions = 1,
        preferredAspectRatio = 3f,
        maxLength = 80,
        titleStringId = R.string.qrcode_compose_lib_format_itf_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_itf_subtitle,
    ),

    /** UPC-A 1D format. */
    BARCODE_US_UPC_A(
        dimensions = 1,
        preferredAspectRatio = 1.5f,
        maxLength = 13,
        titleStringId = R.string.qrcode_compose_lib_format_upc_a_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_upc_a_subtitle,
    ),

    /** UPC-E 1D format. */
    BARCODE_US_UPC_E(
        dimensions = 1,
        preferredAspectRatio = 1.5f,
        maxLength = 8,
        titleStringId = R.string.qrcode_compose_lib_format_upc_e_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_upc_e_subtitle,
    ),

    /** CODABAR 1D format. */
    CODABAR(
        dimensions = 1,
        preferredAspectRatio = 2.5f,
        titleStringId = R.string.qrcode_compose_lib_format_codabar_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_codabar_subtitle,
    ),
}
