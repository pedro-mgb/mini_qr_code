package com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.BuildConfig
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.openAppToView
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppScreen
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppUIAction
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppUIState
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about.SettingsAboutAppViewModel
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.openUrl
import kotlinx.serialization.Serializable

@Serializable
data object AboutAppRoute

private const val GITHUB_REPOSITORY = "https://github.com/pedro-mgb/mini_qr_code"
private const val PRIVACY_POLICY =
    "https://github.com/pedro-mgb/mini_qr_code/" +
        "blob/main/docs/legal/privacy-policy-en.md#mini-qr-code---privacy-policy"
private const val MORE_APPS_URL = "https://play.google.com/store/apps/developer?id=pedroid04"

fun NavGraphBuilder.settingsAboutAppRoute(navigationListeners: SettingsAboutAppNavigationListeners) {
    composable<AboutAppRoute> {
        SettingsAboutAppCoordinator(navigationListeners = navigationListeners)
    }
}

@Composable
private fun SettingsAboutAppCoordinator(
    navigationListeners: SettingsAboutAppNavigationListeners,
    viewModel: SettingsAboutAppViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState: SettingsAboutAppUIState by viewModel.uiState.collectAsStateWithLifecycle()

    SettingsAboutAppScreen(
        appVersion = BuildConfig.VERSION_NAME,
        appBuildNumber = BuildConfig.VERSION_CODE.toString(),
        externalNavigationListeners = navigationListeners,
        actionListeners =
            SettingsAboutAppActionListeners(
                onOpenGithub = {
                    val actionWithResult = context.openUrl(GITHUB_REPOSITORY, uiState.openUrlMode)
                    viewModel.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(actionWithResult))
                },
                onOpenPrivacyPolicy = {
                    val actionWithResult = context.openUrl(PRIVACY_POLICY, uiState.openUrlMode)
                    viewModel.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(actionWithResult))
                },
                onOpenMoreApps = {
                    // not opening in custom tab because this should direct to play store app if user has it installed
                    val actionWithResult = context.openAppToView(MORE_APPS_URL)
                    viewModel.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(actionWithResult))
                },
            ),
    )

    TemporaryMessage(data = uiState.temporaryMessage) {
        viewModel.onNewAction(action = SettingsAboutAppUIAction.TmpMessageShown)
    }
}

fun NavController.navigateToSettingsAboutApp(navOptions: NavOptions? = null) {
    this.navigate(AboutAppRoute, navOptions)
}
