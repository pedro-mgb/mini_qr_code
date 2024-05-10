package com.pedroid.qrcodecompose.androidapp.features.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.sendEmail
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainScreen
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainViewModel

const val SETTINGS_ROUTE = "APP_SETTINGS_ROUTE"
private const val CONTACT_EMAIL = "pedroid.apps.contact@gmail.com"

fun NavGraphBuilder.settingsMainRoute() {
    composable(route = SETTINGS_ROUTE) {
        SettingsMainCoordinator()
    }
}

@Composable
private fun SettingsMainCoordinator(viewModel: SettingsMainViewModel = hiltViewModel()) {
    val context = LocalContext.current

    SettingsMainScreen(
        actionListeners =
            SettingsMainActionListeners(
                onContactDeveloperByEmail = {
                    context.sendEmail(CONTACT_EMAIL)
                },
            ),
    )
}

fun NavController.navigateToMainSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}
