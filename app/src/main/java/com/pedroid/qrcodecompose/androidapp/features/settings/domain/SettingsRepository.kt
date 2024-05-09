package com.pedroid.qrcodecompose.androidapp.features.settings.domain

import kotlinx.coroutines.flow.Flow

interface SettingsRepository : SettingsReadOnlyRepository {
    suspend fun setAppLanguage(language: String)

    suspend fun setOpenUrlPreferences(preferences: OpenUrlPreferences)

    suspend fun setScanHapticFeedback(hapticFeedbackOn: Boolean)

    suspend fun setScanHistorySavePreferences(preferences: HistorySavePreferences)

    suspend fun setGenerateHistorySavePreferences(preferences: HistorySavePreferences)
}

interface SettingsReadOnlyRepository {
    fun getFullSettings(): Flow<FullSettings>
}
