package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QRCodeHistoryDBEntity::class], version = 1)
abstract class QRCodeHistoryDatabase : RoomDatabase() {
    abstract fun qRCodeHistoryDao(): QRCodeHistoryDao
}
