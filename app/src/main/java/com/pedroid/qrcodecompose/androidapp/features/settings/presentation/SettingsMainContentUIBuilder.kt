package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Settings
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Browser
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Language
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import javax.inject.Inject

class SettingsMainContentUIBuilder
    @Inject
    constructor() {
        internal fun generalSettingsItems(
            generalSettings: GeneralSettings,
            currentLanguage: AppLanguage,
        ): SettingsMainContentSection =
            SettingsMainContentSection(
                headerText = "settings_main_screen_header_general",
                headerIcon = Icons.Rounded.Settings,
                items =
                    listOf(
                        SettingsMainContentItem.OptionWithActionSelection(
                            text = "settings_main_screen_general_app_language",
                            actionIcon = Icons.Outlined.Language,
                            currentOption = currentLanguage.translationKey,
                            selectionContent =
                                SelectionContent(
                                    title = "settings_main_screen_general_app_language",
                                    options =
                                        AppLanguage.entries.map {
                                            TitleAndDescription(title = it.translationKey, description = "")
                                        },
                                    onSelect = {
                                        AppLanguage.fromOrdinal(it).let { selectedLanguage ->
                                            SettingsMainUIAction.ChangeAppLanguage(selectedLanguage ?: currentLanguage)
                                        }
                                    },
                                ),
                        ),
                        SettingsMainContentItem.OptionWithActionSelection(
                            text = "settings_main_screen_general_open_urls",
                            actionIcon = Icons.Outlined.Browser,
                            currentOption = generalSettings.openUrlPreferences.createTitleAndDescription().title,
                            selectionContent =
                                SelectionContent(
                                    title = "settings_main_screen_general_open_urls",
                                    options =
                                        OpenUrlPreferences.entries.map {
                                            it.createTitleAndDescription()
                                        },
                                    onSelect = {
                                        OpenUrlPreferences.fromOrdinal(it).let { selected ->
                                            SettingsMainUIAction.ChangeOpenUrlMode(selected ?: generalSettings.openUrlPreferences)
                                        }
                                    },
                                ),
                        ),
                    ),
            )

        internal fun scanSettingsItems(scanSettings: ScanSettings): SettingsMainContentSection =
            SettingsMainContentSection(
                headerText = "settings_main_screen_header_scan",
                headerIcon = Icons.Filled.ScanQRCode,
                items = listOf(),
            )

        internal fun generateSettingsItems(generateSettings: GenerateSettings): SettingsMainContentSection =
            SettingsMainContentSection(
                headerText = "settings_main_screen_header_generate",
                headerIcon = Icons.Filled.AddCircle,
                items = listOf(),
            )

        internal fun otherSettingsItems(): SettingsMainContentSection =
            SettingsMainContentSection(
                headerText = "settings_main_screen_header_other",
                headerIcon = Icons.Outlined.Info,
                items = listOf(),
            )
    }

private fun OpenUrlPreferences.createTitleAndDescription(): TitleAndDescription =
    when (this) {
        OpenUrlPreferences.IN_BROWSER ->
            TitleAndDescription(
                title = "settings_main_screen_general_open_urls_option_browser_title",
                description = "settings_main_screen_general_open_urls_option_browser_description",
            )
        OpenUrlPreferences.IN_CUSTOM_TAB ->
            TitleAndDescription(
                title = "settings_main_screen_general_open_urls_in_app_custom_tab_title",
                description = "settings_main_screen_general_open_urls_in_app_custom_tab_description",
            )
    }

private fun HistorySavePreferences.createScanTitleAndDescription(): TitleAndDescription =
    when (this) {
        HistorySavePreferences.UPON_USER_ACTION ->
            TitleAndDescription(
                title = "settings_main_screen_general_open_urls_option_browser_title",
                description = "settings_main_screen_general_open_urls_option_browser_description",
            )
        HistorySavePreferences.NEVER_SAVE ->
            TitleAndDescription(
                title = "settings_main_screen_general_open_urls_in_app_custom_tab_title",
                description = "settings_main_screen_general_open_urls_in_app_custom_tab_description",
            )
    }

private fun HistorySavePreferences.createGenerateTitleAndDescription(): TitleAndDescription =
    when (this) {
        HistorySavePreferences.UPON_USER_ACTION ->
            TitleAndDescription(
                title = "settings_main_screen_general_open_urls_option_browser_title",
                description = "settings_main_screen_general_open_urls_option_browser_description",
            )
        HistorySavePreferences.NEVER_SAVE ->
            TitleAndDescription(
                title = "settings_main_screen_general_open_urls_in_app_custom_tab_title",
                description = "settings_main_screen_general_open_urls_in_app_custom_tab_description",
            )
    }
