package com.pedroid.qrcodecomposelib.generate

import android.graphics.Bitmap

sealed class QRCodeGenerateResult {
    data class Generated(val bitmap: Bitmap) : QRCodeGenerateResult()

    data class Error(val exception: Exception) : QRCodeGenerateResult()
}
