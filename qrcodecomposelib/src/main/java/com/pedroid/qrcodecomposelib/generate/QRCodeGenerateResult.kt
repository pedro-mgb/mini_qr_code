package com.pedroid.qrcodecomposelib.generate

import android.graphics.Bitmap

/**
 * Error indicating that the input passed is not compliant with the specified [com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat]
 */
typealias QRCodeComposeXNotCompliantWithFormatException = IllegalArgumentException

/**
 * Type of Result of generating QR Code - either successfully Generated or an Error occurred
 */
sealed class QRCodeGenerateResult {
    data class Generated(val bitmap: Bitmap) : QRCodeGenerateResult()

    data class Error(val exception: Exception) : QRCodeGenerateResult()
}
