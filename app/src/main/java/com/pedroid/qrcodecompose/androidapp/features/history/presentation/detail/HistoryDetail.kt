package com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail

import android.os.Parcelable
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryTypeUI
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.Parcelize

@Parcelize
data class HistoryDetail(
    val uid: Long,
    val value: String,
    val format: QRCodeComposeXFormat,
    val displayDate: String,
    val typeUI: HistoryTypeUI,
    val errorInGenerating: Boolean = false,
) : Parcelable
