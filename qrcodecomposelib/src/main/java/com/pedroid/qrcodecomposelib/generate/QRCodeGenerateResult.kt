package com.pedroid.qrcodecomposelib.generate

import android.graphics.Bitmap

sealed interface QRCodeGenerateResult {
    data class Generated(val bitmap: Bitmap) : QRCodeGenerateResult

    data class Error(val exception: Exception) : QRCodeGenerateResult
}