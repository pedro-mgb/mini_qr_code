package com.pedroid.qrcodecompose.androidapp.bridge.di

import android.content.Context
import androidx.room.Room
import com.pedroid.qrcodecompose.androidapp.bridge.data.db.DATABASE_NAME
import com.pedroid.qrcodecompose.androidapp.bridge.data.db.QRCodeDatabase
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseProvides {
    @Provides
    fun providesQRCodeDatabase(
        @ApplicationContext applicationContext: Context,
    ): QRCodeDatabase =
        Room.databaseBuilder(
            applicationContext,
            QRCodeDatabase::class.java,
            DATABASE_NAME,
        ).build()

    @Provides
    fun providesQRCodeHistoryDao(qrCodeHistoryDatabase: QRCodeDatabase): QRCodeHistoryDao = qrCodeHistoryDatabase.qrCodeHistoryDao()
}
