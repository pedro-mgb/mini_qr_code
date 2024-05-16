package com.pedroid.qrcodecompose.androidapp.features.settings.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.SettingsAboutAppNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.navigateToSettingsAboutApp
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.settingsAboutAppRoute
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.SettingsContactNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.navigateToSettingsContact
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.settingsContactRoute

fun NavGraphBuilder.settingsFeatureNavigationRoutes(navController: NavController) {
    settingsMainRoute(
        navigationListeners =
            SettingsMainNavigationListeners(
                onContactDeveloper = {
                    navController.navigateToSettingsContact()
                },
                onMoreAboutApp = {
                    navController.navigateToSettingsAboutApp()
                },
            ),
    )
    settingsContactRoute(
        navigationListeners =
            SettingsContactNavigationListeners(
                onGoBack = {
                    navController.popBackStack()
                },
            ),
    )
    settingsAboutAppRoute(
        navigationListeners =
            SettingsAboutAppNavigationListeners(
                onGoBack = {
                    navController.popBackStack()
                },
            ),
    )
}
