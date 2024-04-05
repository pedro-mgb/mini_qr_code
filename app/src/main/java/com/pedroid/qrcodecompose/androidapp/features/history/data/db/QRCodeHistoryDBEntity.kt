package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO finish adding fields
@Entity(tableName = QRCodeHistoryDBConstants.HISTORY_TABLE)
data class QRCodeHistoryDBEntity(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "code_value") val value: String,
    val timeStamp: Long,
)
