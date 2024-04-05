package com.pedroid.qrcodecompose.androidapp.features.history.di

import android.content.Context
import androidx.room.Room
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDBConstants
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryProvides {
    @Provides
    fun providesQRCodeHistoryDatabase(
        @ApplicationContext applicationContext: Context,
    ) = Room.databaseBuilder(
        applicationContext,
        QRCodeHistoryDatabase::class.java,
        QRCodeHistoryDBConstants.HISTORY_DATABASE,
    )

    @Provides
    fun providesQRCodeHistoryDao(qrCodeHistoryDatabase: QRCodeHistoryDatabase) = qrCodeHistoryDatabase.qRCodeHistoryDao()
}
