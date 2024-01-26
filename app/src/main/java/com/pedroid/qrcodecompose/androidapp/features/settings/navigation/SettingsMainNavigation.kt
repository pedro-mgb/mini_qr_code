package com.pedroid.qrcodecompose.androidapp.features.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainScreen

const val SETTINGS_ROUTE = "APP_SETTINGS_ROUTE"

fun NavGraphBuilder.settingsMainRoute() {
    composable(route = SETTINGS_ROUTE) {
        SettingsMainScreen()
    }
}

fun NavController.navigateToMainSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}