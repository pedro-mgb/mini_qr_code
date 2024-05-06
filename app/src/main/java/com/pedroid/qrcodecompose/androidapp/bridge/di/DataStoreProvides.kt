package com.pedroid.qrcodecompose.androidapp.bridge.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.SettingsProto
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.SettingsProtoSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProvides {

    @Provides
    @Singleton
    fun providesAppSettingsDataStore(
        @ApplicationContext context: Context,
        userPreferencesSerializer: SettingsProtoSerializer,
    ): DataStore<SettingsProto> =
        DataStoreFactory.create(
            serializer = userPreferencesSerializer,
        ) {
            context.dataStoreFile("mini_qr_code_app_settings.pb")
        }
}
