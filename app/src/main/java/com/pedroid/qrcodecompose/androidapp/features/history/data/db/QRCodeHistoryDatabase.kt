package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [QRCodeHistoryDBEntity::class], version = QRCodeHistoryDBConstants.DATABASE_VERSION)
@TypeConverters(QRCodeHistoryDBTypeConverters::class)
abstract class QRCodeHistoryDatabase : RoomDatabase() {
    abstract fun qrCodeHistoryDao(): QRCodeHistoryDao
}
