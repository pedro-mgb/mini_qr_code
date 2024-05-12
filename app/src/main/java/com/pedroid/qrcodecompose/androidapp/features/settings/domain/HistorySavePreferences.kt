package com.pedroid.qrcodecompose.androidapp.features.settings.domain

enum class HistorySavePreferences {
    UPON_USER_ACTION,
    NEVER_SAVE,
    ;

    companion object {
        val DEFAULT = UPON_USER_ACTION

        fun fromOrdinal(ordinal: Int): HistorySavePreferences? = HistorySavePreferences.entries.find { it.ordinal == ordinal }
    }
}
