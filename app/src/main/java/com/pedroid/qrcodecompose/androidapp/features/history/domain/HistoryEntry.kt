package com.pedroid.qrcodecompose.androidapp.features.history.domain

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import java.util.Date

// TODO finish adding fields
sealed class HistoryEntry(
    open val uid: Long,
    open val value: String,
    open val creationDate: Date,
    open val format: QRCodeComposeXFormat,
) {
    // TODO add attributes for actions made with each code
    data class ScanEntry(
        override val uid: Long,
        override val value: String,
        override val creationDate: Date,
        override val format: QRCodeComposeXFormat,
        val fromImage: Boolean,
    ) : HistoryEntry(uid, value, creationDate, format)

    data class GenerateEntry(
        override val uid: Long,
        override val value: String,
        override val creationDate: Date,
        override val format: QRCodeComposeXFormat,
    ) : HistoryEntry(uid, value, creationDate, format)
}
