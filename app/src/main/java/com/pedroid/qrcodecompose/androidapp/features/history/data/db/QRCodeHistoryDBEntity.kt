package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pedroid.qrcodecompose.androidapp.features.history.data.HistoryType
import com.pedroid.qrcodecompose.androidapp.features.history.domain.ID_NEW_ENTRY
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import java.time.Instant

// TODO finish adding fields
@Entity(tableName = QRCodeHistoryDBConstants.HISTORY_TABLE)
data class QRCodeHistoryDBEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long = ID_NEW_ENTRY,
    val type: HistoryType,
    @ColumnInfo(name = "code_value") val value: String,
    val timeStamp: Instant,
    val format: QRCodeComposeXFormat,
)
