package com.pedroid.qrcodecompose.androidapp.features.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository : SettingsReadOnlyRepository {
    // TODO add methods to modify settings
}

interface SettingsReadOnlyRepository {
    fun getFullSettings(): Flow<FullSettings>
}
