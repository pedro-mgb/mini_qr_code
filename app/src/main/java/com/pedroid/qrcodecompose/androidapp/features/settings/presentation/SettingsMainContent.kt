package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsMainContentSection(
    val headerText: String,
    val headerIcon: ImageVector,
    val items: List<SettingsMainContentItem>,
)

sealed class SettingsMainContentItem {
    data class OptionWithExternalScreenAction(
        val text: String,
        val actionIcon: ImageVector,
        val actionContext: String = "",
    ) : SettingsMainContentItem()

    data class OptionWithActionSelection(
        val text: String,
        val actionIcon: ImageVector,
        val currentOption: String,
        val selectionContent: SelectionContent,
    ) : SettingsMainContentItem()

    data class OptionWithToggle(
        val text: String,
        val actionIcon: ImageVector,
        val toggleOn: Boolean,
        val toggleContext: String,
        val toggleAction: SettingsMainUIAction,
    ) : SettingsMainContentItem()
}

data class SelectionContent(
    val title: String,
    val options: List<TitleAndDescription>,
    val onSelect: (Int) -> SettingsMainUIAction,
)

data class TitleAndDescription(val title: String, val description: String)
