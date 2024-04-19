package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.TypeConverter
import com.pedroid.qrcodecompose.androidapp.features.history.data.HistoryType
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import java.time.Instant

class QRCodeHistoryDBTypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long): Instant = Instant.ofEpochMilli(value)

    @TypeConverter
    fun instantToTimestamp(instant: Instant): Long = instant.toEpochMilli()

    @TypeConverter
    fun fromHistoryTypeString(value: String): HistoryType {
        return HistoryType.entries.first { value == it.name }
    }

    @TypeConverter
    fun historyTypeToString(type: HistoryType): String = type.name

    @TypeConverter
    fun fromQRCodeComposeXFormatString(value: String): QRCodeComposeXFormat {
        return QRCodeComposeXFormat.entries.first { value == it.name }
    }

    @TypeConverter
    fun qrCodeComposeXFormatToString(format: QRCodeComposeXFormat): String = format.name
}
