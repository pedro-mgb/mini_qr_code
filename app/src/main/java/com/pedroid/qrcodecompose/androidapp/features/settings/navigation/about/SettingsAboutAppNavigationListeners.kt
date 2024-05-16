package com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about

data class SettingsAboutAppNavigationListeners(
    val onGoBack: () -> Unit = {},
)

data class SettingsAboutAppActionListeners(
    val onOpenGithub: () -> Unit = {},
    val onOpenPrivacyPolicy: () -> Unit = {},
    val onOpenMoreApps: () -> Unit = {},
)
