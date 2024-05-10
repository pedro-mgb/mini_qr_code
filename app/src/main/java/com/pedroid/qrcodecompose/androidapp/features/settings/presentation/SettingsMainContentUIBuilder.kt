package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import javax.inject.Inject

class SettingsMainContentUIBuilder
    @Inject
    constructor() {
        internal fun generalSettingsItems(generalSettings: GeneralSettings): SettingsMainContentSection =
            SettingsMainContentSection(
                header = "settings_main_screen_header_general",
                items =
                    listOf(
                        SettingsMainContentItem.OptionWithActionInScreen(
                            text = "",
                            currentOption = "",
                        ),
                    ),
            )

        internal fun scanSettingsItems(scanSettings: ScanSettings): SettingsMainContentSection =
            SettingsMainContentSection(
                header = "settings_main_screen_header_scan",
                items = listOf(),
            )

        internal fun generateSettingsItems(generateSettings: GenerateSettings): SettingsMainContentSection =
            SettingsMainContentSection(
                header = "settings_main_screen_header_generate",
                items = listOf(),
            )

        internal fun otherSettingsItems(): SettingsMainContentSection =
            SettingsMainContentSection(
                header = "settings_main_screen_header_other",
                items = listOf(),
            )
    }
