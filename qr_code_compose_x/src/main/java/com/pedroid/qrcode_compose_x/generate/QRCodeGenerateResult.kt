package com.pedroid.qrcode_compose_x.generate

import android.graphics.Bitmap

sealed interface QRCodeGenerateResult {
    data class Generated(val bitmap: Bitmap) : QRCodeGenerateResult

    data object Error : QRCodeGenerateResult
}