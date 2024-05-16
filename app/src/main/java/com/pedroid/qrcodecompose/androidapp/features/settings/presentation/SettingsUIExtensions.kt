package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import android.content.Context
import androidx.compose.ui.graphics.toArgb
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.launchBrowserCustomTab
import com.pedroid.qrcodecompose.androidapp.core.presentation.openAppToView
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.md_theme_dark_background
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences

fun Context.openUrl(
    content: String,
    preferences: OpenUrlPreferences,
    customTabToolbarColor: Int = md_theme_dark_background.toArgb(),
): QRAppActions.OpenApp {
    return when (preferences) {
        OpenUrlPreferences.IN_CUSTOM_TAB -> this.launchBrowserCustomTab(content, customTabToolbarColor)
        OpenUrlPreferences.IN_BROWSER -> this.openAppToView(content)
    }
}
