package com.pedroid.qrcodecompose.androidapp.features.settings.domain

enum class OpenUrlPreferences {
    IN_BROWSER,
    IN_CUSTOM_TAB,
    ;

    companion object {
        val DEFAULT = IN_BROWSER

        fun fromOrdinal(ordinal: Int): OpenUrlPreferences? = OpenUrlPreferences.entries.find { it.ordinal == ordinal }
    }
}
