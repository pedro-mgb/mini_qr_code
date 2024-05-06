package com.pedroid.qrcodecompose.androidapp.features.settings.di

import com.pedroid.qrcodecompose.androidapp.features.settings.data.QRCodeSettingsRepository
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsReadOnlyRepository
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SettingsBinds {
    @Singleton
    @Binds
    fun bindQRCodeSettingsReadOnlyRepository(qrCodeSettingsRepository: QRCodeSettingsRepository): SettingsReadOnlyRepository

    @Singleton
    @Binds
    fun bindQRCodeSettingsRepository(qrCodeSettingsRepository: QRCodeSettingsRepository): SettingsRepository
}
