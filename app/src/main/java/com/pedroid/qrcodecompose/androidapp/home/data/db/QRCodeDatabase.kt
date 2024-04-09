package com.pedroid.qrcodecompose.androidapp.home.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDBEntity
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDBTypeConverters
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDao

const val DATABASE_NAME = "qrCodeDatabase"
const val DATABASE_VERSION = 1

@Database(entities = [QRCodeHistoryDBEntity::class], version = DATABASE_VERSION)
@TypeConverters(QRCodeHistoryDBTypeConverters::class)
abstract class QRCodeDatabase : RoomDatabase() {
    abstract fun qrCodeHistoryDao(): QRCodeHistoryDao
}
