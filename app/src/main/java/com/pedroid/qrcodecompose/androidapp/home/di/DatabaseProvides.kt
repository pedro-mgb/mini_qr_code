package com.pedroid.qrcodecompose.androidapp.home.di

import android.content.Context
import androidx.room.Room
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDao
import com.pedroid.qrcodecompose.androidapp.home.data.db.DATABASE_NAME
import com.pedroid.qrcodecompose.androidapp.home.data.db.QRCodeDatabase
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
    ) = Room.databaseBuilder(
        applicationContext,
        QRCodeDatabase::class.java,
        DATABASE_NAME,
    )

    @Provides
    fun providesQRCodeHistoryDao(qrCodeHistoryDatabase: QRCodeDatabase): QRCodeHistoryDao = qrCodeHistoryDatabase.qrCodeHistoryDao()
}
