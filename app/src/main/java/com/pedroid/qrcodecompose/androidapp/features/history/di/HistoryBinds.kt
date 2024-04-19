package com.pedroid.qrcodecompose.androidapp.features.history.di

import com.pedroid.qrcodecompose.androidapp.features.history.data.QRCodeHistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface HistoryBinds {
    @Singleton
    @Binds
    fun bindQRCodeHistoryRepository(qrCodeHistoryRepository: QRCodeHistoryRepository): HistoryRepository
}
