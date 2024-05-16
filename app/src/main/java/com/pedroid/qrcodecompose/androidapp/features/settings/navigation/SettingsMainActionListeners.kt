package com.pedroid.qrcodecompose.androidapp.features.settings.navigation

import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsExternalAction
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainContentItem
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainUIAction

data class SettingsMainNavigationListeners(
    val onContactDeveloper: () -> Unit,
    val onMoreAboutApp: () -> Unit,
)

data class SettingsMainActionListeners(
    val onShowSettingsOptionSelection: (SettingsMainContentItem.OptionWithActionSelection) -> Unit = {},
    val onUIAction: (SettingsMainUIAction) -> Unit = {},
    val onExternalNavigationAction: (SettingsExternalAction) -> Unit = {},
)
