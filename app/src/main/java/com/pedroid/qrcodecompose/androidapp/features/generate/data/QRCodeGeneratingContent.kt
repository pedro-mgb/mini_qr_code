package com.pedroid.qrcodecompose.androidapp.features.generate.data

import android.os.Parcelable
import arrow.optics.optics
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@optics
@Parcelize
data class QRCodeGeneratingContent(
    val qrCodeText: String = "",
    val format: QRCodeComposeXFormat = QRCodeComposeXFormat.QR_CODE,
) : Parcelable {
    @IgnoredOnParcel
    val empty: Boolean = qrCodeText.isBlank()

    companion object
}
