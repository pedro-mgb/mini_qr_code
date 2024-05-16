package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.rounded.Settings
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Browser
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.ContactSupport
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Language
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.SaveAlt
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Smartphone
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Vibration
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
                                    options = OpenUrlPreferences.entries.map { it.createTitleAndDescription() },
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
                items =
                    listOf(
                        SettingsMainContentItem.OptionWithActionSelection(
                            text = "settings_main_screen_scan_save_history",
                            actionIcon = Icons.Outlined.SaveAlt,
                            currentOption = scanSettings.historySave.createScanTitleAndDescription().title,
                            selectionContent =
                                SelectionContent(
                                    options = HistorySavePreferences.entries.map { it.createScanTitleAndDescription() },
                                    onSelect = {
                                        HistorySavePreferences.fromOrdinal(it).let { selected ->
                                            SettingsMainUIAction.ChangeScanHistorySave(selected ?: scanSettings.historySave)
                                        }
                                    },
                                ),
                        ),
                        SettingsMainContentItem.OptionWithToggle(
                            text = "settings_main_screen_scan_haptic_feedback",
                            actionIcon = Icons.Outlined.Vibration,
                            toggleOn = scanSettings.hapticFeedback,
                            toggleContext =
                                if (scanSettings.hapticFeedback) {
                                    "settings_main_screen_scan_haptic_feedback_status_on"
                                } else {
                                    "settings_main_screen_scan_haptic_feedback_status_off"
                                },
                            toggleAction = SettingsMainUIAction.ToggleScanHapticFeedback,
                        ),
                    ),
            )

        internal fun generateSettingsItems(generateSettings: GenerateSettings): SettingsMainContentSection =
            SettingsMainContentSection(
                headerText = "settings_main_screen_header_generate",
                headerIcon = Icons.Filled.AddCircle,
                items =
                    listOf(
                        SettingsMainContentItem.OptionWithActionSelection(
                            text = "settings_main_screen_generate_save_history",
                            actionIcon = Icons.Outlined.SaveAlt,
                            currentOption = generateSettings.historySave.createGenerateTitleAndDescription().title,
                            selectionContent =
                                SelectionContent(
                                    options = HistorySavePreferences.entries.map { it.createGenerateTitleAndDescription() },
                                    onSelect = {
                                        HistorySavePreferences.fromOrdinal(it).let { selected ->
                                            SettingsMainUIAction.ChangeGenerateHistorySave(selected ?: generateSettings.historySave)
                                        }
                                    },
                                ),
                        ),
                    ),
            )

        internal fun otherSettingsItems(): SettingsMainContentSection =
            SettingsMainContentSection(
                headerText = "settings_main_screen_header_other",
                headerIcon = Icons.Outlined.Info,
                items =
                    listOf(
                        SettingsMainContentItem.OptionWithExternalScreenAction(
                            text = "settings_main_screen_other_contact_developer_title",
                            actionIcon = Icons.Outlined.ContactSupport,
                            actionContext = "settings_main_screen_other_contact_developer_description",
                            action = SettingsExternalAction.ContactDeveloper,
                        ),
                        SettingsMainContentItem.OptionWithExternalScreenAction(
                            text = "settings_main_screen_other_rate_app_title",
                            actionIcon = Icons.Outlined.ThumbUp,
                            actionContext = "settings_main_screen_other_rate_app_description",
                            action = SettingsExternalAction.RateApp,
                        ),
                        SettingsMainContentItem.OptionWithExternalScreenAction(
                            text = "settings_main_screen_other_about_the_app_title",
                            actionIcon = Icons.Outlined.Smartphone,
                            actionContext = "settings_main_screen_other_about_the_app_description",
                            action = SettingsExternalAction.MoreInfoAboutApp,
                        ),
                    ),
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
                title = "settings_main_screen_scan_save_history_option_upon_user_action_title",
                description = "settings_main_screen_scan_save_history_option_upon_user_action_description",
            )
        HistorySavePreferences.NEVER_SAVE ->
            TitleAndDescription(
                title = "settings_main_screen_scan_save_history_option_never_title",
                description = "settings_main_screen_scan_save_history_option_never_description",
            )
    }

private fun HistorySavePreferences.createGenerateTitleAndDescription(): TitleAndDescription =
    when (this) {
        HistorySavePreferences.UPON_USER_ACTION ->
            TitleAndDescription(
                title = "settings_main_screen_generate_save_history_option_upon_user_action_title",
                description = "settings_main_screen_generate_save_history_option_upon_user_action_description",
            )
        HistorySavePreferences.NEVER_SAVE ->
            TitleAndDescription(
                title = "settings_main_screen_generate_save_history_option_never_title",
                description = "settings_main_screen_generate_save_history_option_never_description",
            )
    }
