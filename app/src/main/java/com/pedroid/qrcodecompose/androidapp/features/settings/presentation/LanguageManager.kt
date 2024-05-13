package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class LanguageManager
    @Inject
    constructor() {
        private val appLanguageFlow = MutableStateFlow(getAppLanguage())

        fun getAppLanguage(): AppLanguage = AppLanguage.fromLocaleList(AppCompatDelegate.getApplicationLocales())

        fun getAppLanguageFlow(): Flow<AppLanguage> = appLanguageFlow.asStateFlow()

        suspend fun setAppLanguage(language: AppLanguage) {
            appLanguageFlow.emit(language)
            if (language == AppLanguage.SAME_AS_SYSTEM) {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
            } else {
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.langCode))
            }
        }
    }

enum class AppLanguage(
    val langCode: String,
    val translationKey: String,
) {
    SAME_AS_SYSTEM("", "settings_main_screen_general_app_language_option_same_as_system"),
    ENGLISH("en", "settings_main_screen_general_app_language_option_english"),
    PORTUGUESE("pt", "settings_main_screen_general_app_language_option_portuguese"),
    ;

    companion object {
        internal fun fromLocaleList(localeList: LocaleListCompat): AppLanguage {
            return localeList.getFirstMatch(
                AppLanguage.entries
                    .filterNot { it == SAME_AS_SYSTEM }
                    .map { it.langCode }
                    .toTypedArray(),
            ).let {
                if (it == null) {
                    SAME_AS_SYSTEM
                } else {
                    fromLanguageTag(it.toLanguageTag())
                }
            }
        }

        fun fromOrdinal(ordinal: Int): AppLanguage? {
            return AppLanguage.entries.find {
                it.ordinal == ordinal
            }
        }

        private fun fromLanguageTag(tag: String): AppLanguage {
            return AppLanguage.entries.find {
                it.langCode == tag
            } ?: SAME_AS_SYSTEM
        }
    }
}
