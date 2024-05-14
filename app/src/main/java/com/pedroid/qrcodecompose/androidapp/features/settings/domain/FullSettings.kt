package com.pedroid.qrcodecompose.androidapp.features.settings.domain

data class FullSettings(
    val general: GeneralSettings = GeneralSettings(),
    val scan: ScanSettings = ScanSettings(),
    val generate: GenerateSettings = GenerateSettings(),
)

const val HAPTIC_FEEDBACK_DEFAULT_OFF = false

data class GeneralSettings(
    val openUrlPreferences: OpenUrlPreferences = OpenUrlPreferences.DEFAULT,
)

data class ScanSettings(
    val hapticFeedback: Boolean = HAPTIC_FEEDBACK_DEFAULT_OFF,
    val historySave: HistorySavePreferences = HistorySavePreferences.DEFAULT,
)

data class GenerateSettings(
    val historySave: HistorySavePreferences = HistorySavePreferences.DEFAULT,
)
