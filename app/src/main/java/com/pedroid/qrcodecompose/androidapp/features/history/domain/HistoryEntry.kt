package com.pedroid.qrcodecompose.androidapp.features.history.domain

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import java.time.Instant

// when creating a new entry in history, to avoid specifying an id, use this new one
const val ID_NEW_ENTRY: Long = 0L

// TODO finish adding fields
sealed class HistoryEntry(
    open val uid: Long,
    open val value: String,
    open val creationDate: Instant,
    open val format: QRCodeComposeXFormat,
) {
    // TODO add attributes for actions made with each code
    data class ScanEntry(
        override val uid: Long,
        override val value: String,
        override val creationDate: Instant,
        override val format: QRCodeComposeXFormat,
        val fromImage: Boolean,
    ) : HistoryEntry(uid, value, creationDate, format)

    data class GenerateEntry(
        override val uid: Long,
        override val value: String,
        override val creationDate: Instant,
        override val format: QRCodeComposeXFormat,
    ) : HistoryEntry(uid, value, creationDate, format)
}
