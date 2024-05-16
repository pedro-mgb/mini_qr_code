package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.ui.graphics.vector.ImageVector

data class SettingsMainContentSection(
    val headerText: String,
    val headerIcon: ImageVector,
    val items: List<SettingsMainContentItem>,
)

sealed class SettingsMainContentItem(
    open val text: String,
    open val context: String,
    open val startIcon: ImageVector,
) {
    data class OptionWithExternalScreenAction(
        override val text: String,
        val actionIcon: ImageVector,
        val actionContext: String = "",
        val action: SettingsExternalAction,
    ) : SettingsMainContentItem(text, actionContext, actionIcon)

    data class OptionWithActionSelection(
        override val text: String,
        val actionIcon: ImageVector,
        val currentOption: String,
        val selectionContent: SelectionContent,
    ) : SettingsMainContentItem(text, currentOption, actionIcon)

    data class OptionWithToggle(
        override val text: String,
        val actionIcon: ImageVector,
        val toggleOn: Boolean,
        val toggleContext: String,
        val toggleAction: SettingsMainUIAction,
    ) : SettingsMainContentItem(text, toggleContext, actionIcon)
}

data class SelectionContent(
    val options: List<TitleAndDescription>,
    val onSelect: (Int) -> SettingsMainUIAction,
)

data class TitleAndDescription(val title: String, val description: String)

sealed interface SettingsExternalAction {
    data object ContactDeveloper : SettingsExternalAction

    data object RateApp : SettingsExternalAction

    data object MoreInfoAboutApp : SettingsExternalAction
}
