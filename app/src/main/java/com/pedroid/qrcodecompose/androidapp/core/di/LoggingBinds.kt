package com.pedroid.qrcodecompose.androidapp.core.di

import com.pedroid.qrcodecompose.androidapp.core.logging.AndroidLogger
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface LoggingBinds {
    @Binds
    fun bindsAndroidLogger(logger: AndroidLogger): Logger
}
