package com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.sendEmail
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppUIAction
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppUIState
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppViewModel
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.contact.SettingsContactScreen
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.openUrl

private const val CONTACT_EMAIL = "pedroid.apps.contact@gmail.com"
private const val GITHUB_ISSUE_URL = "https://github.com/pedro-mgb/mini_qr_code/issues/new"
const val CONTACT_ROUTE = "CONTACT_DEVELOPER_ROUTE"

fun NavGraphBuilder.settingsContactRoute(navigationListeners: SettingsContactNavigationListeners) {
    composable(CONTACT_ROUTE) {
        SettingsContactCoordinator(navigationListeners = navigationListeners)
    }
}

/*
It is not a mistake, SettingsContactCoordinator is using SettingsAboutAppViewModel
Both this and SettingsAboutCoordinator would have the same UI State, hence they are sharing the same View Model for sake of simplicity
In the future, the UI and logic changes between the two, they will be separated in two ViewModels
 */
@Composable
private fun SettingsContactCoordinator(
    navigationListeners: SettingsContactNavigationListeners,
    viewModel: SettingsAboutAppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState: SettingsAboutAppUIState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsContactScreen(
        externalNavigationListeners = navigationListeners,
        actionListeners =
            SettingsContactActionListeners(
                onContactViaGithubIssue = {
                    val actionWithResult = context.openUrl(GITHUB_ISSUE_URL, uiState.openUrlMode)
                    viewModel.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(actionWithResult))
                },
                onContactViaEmail = {
                    context.sendEmail(CONTACT_EMAIL)
                },
            ),
    )

    TemporaryMessage(data = uiState.temporaryMessage) {
        viewModel.onNewAction(action = SettingsAboutAppUIAction.TmpMessageShown)
    }
}

fun NavController.navigateToSettingsContact(navOptions: NavOptions? = null) {
    this.navigate(CONTACT_ROUTE, navOptions)
}
