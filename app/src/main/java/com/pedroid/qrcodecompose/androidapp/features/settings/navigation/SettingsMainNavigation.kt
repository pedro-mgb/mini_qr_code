package com.pedroid.qrcodecompose.androidapp.features.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsExternalAction
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainContentItem
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainScreen
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainUIState
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsMainViewModel
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.SettingsOptionSelectionBottomSheet

const val SETTINGS_ROUTE = "APP_SETTINGS_ROUTE"
private const val CONTACT_EMAIL = "pedroid.apps.contact@gmail.com"

fun NavGraphBuilder.settingsMainRoute(navigationListeners: SettingsMainNavigationListeners) {
    composable(route = SETTINGS_ROUTE) {
        SettingsMainCoordinator(navigationListeners)
    }
}

@Composable
private fun SettingsMainCoordinator(
    navigationListeners: SettingsMainNavigationListeners,
    viewModel: SettingsMainViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var optionWithActionBottomSheet: SettingsMainContentItem.OptionWithActionSelection? by remember {
        mutableStateOf(null)
    }

    SettingsMainScreen(
        content = (uiState as? SettingsMainUIState.Content) ?: SettingsMainUIState.emptyContent(),
        actionListeners =
            SettingsMainActionListeners(
                onShowSettingsOptionSelection = {
                    optionWithActionBottomSheet = it
                },
                onUIAction = viewModel::onNewAction,
                onExternalNavigationAction = {
                    when (it) {
                        SettingsExternalAction.ContactDeveloper -> {
                            navigationListeners.onContactDeveloper()
                        }

                        SettingsExternalAction.RateApp -> {
                            // TODO implement
                        }

                        SettingsExternalAction.MoreInfoAboutApp -> {
                            navigationListeners.onMoreAboutApp()
                        }
                    }
                },
            ),
    )

    optionWithActionBottomSheet?.let {
        SettingsOptionSelectionBottomSheet(
            option = it,
            actionListener = viewModel::onNewAction,
            onDismiss = {
                optionWithActionBottomSheet = null
            },
        )
    }
}

fun NavController.navigateToMainSettings(navOptions: NavOptions? = null) {
    this.navigate(SETTINGS_ROUTE, navOptions)
}
