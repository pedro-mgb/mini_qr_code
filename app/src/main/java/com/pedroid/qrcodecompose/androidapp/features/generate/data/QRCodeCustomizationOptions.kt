package com.pedroid.qrcodecompose.androidapp.features.generate.data

import android.os.Parcelable
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.Parcelize

@Parcelize
data class QRCodeCustomizationOptions(
    val format: QRCodeComposeXFormat = QRCodeComposeXFormat.QR_CODE,
) : Parcelable
