package com.pedroid.qrcodecompose.androidapp.features.settings.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.sendEmail
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainScreen

const val SETTINGS_ROUTE = "APP_SETTINGS_ROUTE"

fun NavGraphBuilder.settingsMainRoute() {
    composable(route = SETTINGS_ROUTE) {
        val context = LocalContext.current

        SettingsMainScreen(
            actionListeners =
                SettingsMainActionListeners(
                    onContactDeveloperByEmail = {
                        context.sendEmail("pedroid.apps.contact@gmail.com")
                    },
                ),
        )
    }
}

fun NavController.navigateToMainSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}
