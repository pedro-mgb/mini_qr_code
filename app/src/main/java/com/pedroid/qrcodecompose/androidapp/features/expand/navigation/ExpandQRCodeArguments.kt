package com.pedroid.qrcodecompose.androidapp.features.expand.navigation

import android.os.Parcelable
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class ExpandQRCodeArguments(
    val label: String,
    val code: String,
    val format: QRCodeComposeXFormat,
) : Parcelable
