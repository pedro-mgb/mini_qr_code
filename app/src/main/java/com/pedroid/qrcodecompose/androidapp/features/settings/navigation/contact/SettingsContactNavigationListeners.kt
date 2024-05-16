package com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact

data class SettingsContactNavigationListeners(
    val onGoBack: () -> Unit = {},
)

data class SettingsContactActionListeners(
    val onContactViaGithubIssue: () -> Unit = {},
    val onContactViaEmail: () -> Unit = {},
)
