package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsMainContentSection(
    val header: String,
    val items: List<SettingsMainContentItem>,
)

sealed class SettingsMainContentItem {
    data class OptionWithExternalScreenAction(
        val text: String,
        val actionIcon: ImageVector,
        val currentOption: String = "",
    ) : SettingsMainContentItem()

    data class OptionWithActionInScreen(
        val text: String,
        val currentOption: String,
    ) : SettingsMainContentItem()

    data class OptionWithToggle(
        val text: String,
        val toggleOn: String,
    ) : SettingsMainContentItem()
}
