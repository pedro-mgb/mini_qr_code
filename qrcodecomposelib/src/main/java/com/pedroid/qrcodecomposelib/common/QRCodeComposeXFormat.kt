package com.pedroid.qrcodecomposelib.common

import androidx.annotation.StringRes
import androidx.compose.ui.text.input.KeyboardType
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
 *      for some it's ".*" -> matches anything
 * @property maxLength maximum length of the content for the format
 * @property inputType input type to restrict characters that can be inputted. By default can input all kinds of text
 * @property titleStringId Unique ID for string resource for title of the format
 * @property subtitleStringId Unique ID for string resource for subtitle containing short description of the format
 * @property errorMessageStringId Unique ID for string resource for error message when QR Code input is malformed,
 *      not compliant with the standard. String resource may have placeholder for content length
 */
enum class QRCodeComposeXFormat(
    val dimensions: Int,
    val preferredAspectRatio: Float,
    val validationRegex: Regex = ".*".toRegex(),
    val maxLength: Int = Int.MAX_VALUE,
    val inputType: KeyboardType = KeyboardType.Text,
    @StringRes val titleStringId: Int,
    @StringRes val subtitleStringId: Int,
    @StringRes val errorMessageStringId: Int = R.string.qr_code_compose_lib_format_default_error_message,
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

    /** Data Matrix 2D barcode format. Supports characters in ISO/IEC 8859-1 */
    DATA_MATRIX(
        dimensions = 2,
        preferredAspectRatio = 1f,
        validationRegex = "^[\\u0000-\\u00FF]*\$".toRegex(),
        titleStringId = R.string.qrcode_compose_lib_format_data_matrix_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_data_matrix_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_data_matrix_error_message,
    ),

    /** PDF417 2D format. */
    PDF_417(
        dimensions = 2,
        preferredAspectRatio = 3f,
        titleStringId = R.string.qrcode_compose_lib_format_pdf_417_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_pdf417_subtitle,
    ),

    /** Code 39 1D format. Supports ASCII characters */
    BARCODE_39(
        dimensions = 1,
        preferredAspectRatio = 2f,
        validationRegex = "^[\\x00-\\x7F]{1,80}\$".toRegex(),
        maxLength = 80,
        inputType = KeyboardType.Ascii,
        titleStringId = R.string.qrcode_compose_lib_format_barcode39_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_barcode39_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_barcode39_error_message,
    ),

    /** Code 93 1D format. For some reason the underlying implementation only supports capital letters and some special characters */
    BARCODE_93(
        dimensions = 1,
        preferredAspectRatio = 2f,
        validationRegex = "^[A-Z0-9\\-.$/+% ]{1,80}$".toRegex(),
        maxLength = 80,
        inputType = KeyboardType.Ascii,
        titleStringId = R.string.qrcode_compose_lib_format_barcode93_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_barcode93_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_barcode93_error_message,
    ),

    /** Code 128 1D format. Supports ASCII characters */
    BARCODE_128(
        dimensions = 1,
        preferredAspectRatio = 2f,
        validationRegex = "^[\\x00-\\x7F]{1,80}\$".toRegex(),
        maxLength = 80,
        inputType = KeyboardType.Ascii,
        titleStringId = R.string.qrcode_compose_lib_format_barcode128_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_barcode128_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_barcode128_error_message,
    ),

    /** EAN-8 1D format. */
    BARCODE_EUROPE_EAN_8(
        dimensions = 1,
        preferredAspectRatio = 1.2f,
        validationRegex = "^\\d{7,8}$".toRegex(),
        maxLength = 8,
        inputType = KeyboardType.NumberPassword,
        titleStringId = R.string.qrcode_compose_lib_format_ean8_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_ean8_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_ean8_error_message,
    ),

    /** EAN-13 1D format. */
    BARCODE_EUROPE_EAN_13(
        dimensions = 1,
        preferredAspectRatio = 1.285f,
        validationRegex = "^\\d{12,13}$".toRegex(),
        maxLength = 13,
        inputType = KeyboardType.NumberPassword,
        titleStringId = R.string.qrcode_compose_lib_format_ean13_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_ean13_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_ean13_error_message,
    ),

    /** ITF (Interleaved Two of Five) 1D format. AKA ITF-14 */
    BARCODE_ITF(
        dimensions = 1,
        preferredAspectRatio = 3f,
        validationRegex = "^(\\d\\d){1,80}".toRegex(),
        maxLength = 80,
        inputType = KeyboardType.NumberPassword,
        titleStringId = R.string.qrcode_compose_lib_format_itf_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_itf_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_itf_error_message,
    ),

    /** UPC-A 1D format. */
    BARCODE_US_UPC_A(
        dimensions = 1,
        preferredAspectRatio = 1.5f,
        validationRegex = "^\\d{12,13}$".toRegex(),
        maxLength = 13,
        inputType = KeyboardType.NumberPassword,
        titleStringId = R.string.qrcode_compose_lib_format_upc_a_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_upc_a_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_upc_a_error_message,
    ),

    /** UPC-E 1D format. */
    BARCODE_US_UPC_E(
        dimensions = 1,
        preferredAspectRatio = 1.5f,
        validationRegex = "^\\d{7,8}$".toRegex(),
        maxLength = 8,
        inputType = KeyboardType.NumberPassword,
        titleStringId = R.string.qrcode_compose_lib_format_upc_e_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_upc_e_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_upc_e_error_message,
    ),

    /** CODABAR 1D format. */
    CODABAR(
        dimensions = 1,
        preferredAspectRatio = 2.5f,
        validationRegex = "^\\d+$".toRegex(),
        inputType = KeyboardType.NumberPassword,
        titleStringId = R.string.qrcode_compose_lib_format_codabar_title,
        subtitleStringId = R.string.qrcode_compose_lib_format_codabar_subtitle,
        errorMessageStringId = R.string.qr_code_compose_lib_format_codabar_error_message,
    ),
}
