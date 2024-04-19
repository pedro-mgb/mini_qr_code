package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class HistoryListItem : Parcelable {
    @Parcelize
    data class SectionHeader(val text: String) : HistoryListItem()

    @Parcelize
    data class Data(
        val uid: Long,
        val value: String,
        val displayDate: String,
        val typeUI: HistoryTypeUI,
        @StringRes val formatStringId: Int,
    ) : HistoryListItem()
}
