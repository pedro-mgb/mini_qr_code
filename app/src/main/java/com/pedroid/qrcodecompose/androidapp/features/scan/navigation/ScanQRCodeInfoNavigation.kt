package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import android.Manifest
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.copyTextToClipboard
import com.pedroid.qrcodecompose.androidapp.core.presentation.launchPermissionRequestOrRun
import com.pedroid.qrcodecompose.androidapp.core.presentation.rememberPermissionState
import com.pedroid.qrcodecompose.androidapp.core.presentation.shareTextToAnotherApp
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeInfoUIAction
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeInfoUIState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeInfoScreen
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeInfoViewModel
import com.pedroid.qrcodecompose.androidapp.features.settings.presentation.openUrl
import kotlinx.serialization.Serializable

@Serializable
data object ScanQRCodeInfoRoute

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.scanQRCodeInfoRoute(
    navigationListeners: ScanQRCodeInfoNavigationListeners,
    largeScreen: Boolean,
    sharedTransitionScope: SharedTransitionScope,
) {
    composable<ScanQRCodeInfoRoute> {
        ScanCodeHomeCoordinator(
            navigationListeners,
            largeScreen,
            it.savedStateHandle,
            sharedTransitionScope,
            animatedVisibilityScope = this@composable,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@ExperimentalSharedTransitionApi
@Composable
private fun ScanCodeHomeCoordinator(
    navigationListeners: ScanQRCodeInfoNavigationListeners,
    largeScreen: Boolean,
    savedStateHandle: SavedStateHandle,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: ScanQRCodeInfoViewModel = hiltViewModel(),
) {
    val cameraPermissionState =
        rememberPermissionState(
            permission = Manifest.permission.CAMERA,
            onPermissionGranted = {
                navigationListeners.onGoScanQRCodeWithCamera()
            },
        )
    (savedStateHandle.get<ScannedCode>(QR_CODE_SCANNED_KEY)).let {
        viewModel.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = it))
    }
    val uiState: QRCodeInfoUIState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ScanQRCodeInfoScreen(
        buttonListeners =
            StartScanActionListeners(
                onStartScanFromCamera = {
                    cameraPermissionState.launchPermissionRequestOrRun {
                        navigationListeners.onGoScanQRCodeWithCamera()
                    }
                },
                onStartScanFromImageFile = {
                    navigationListeners.onGoScanQRCodeFromFile()
                },
            ),
        actionListeners =
            ScannedQRCodeActionListeners(
                onCodeCopied = {
                    context.copyTextToClipboard(it)
                    viewModel.onNewAction(
                        QRCodeInfoUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)),
                    )
                },
                onCodeOpen = {
                    val openActionResult = context.openUrl(it, uiState.openUrlMode)
                    viewModel.onNewAction(QRCodeInfoUIAction.QRActionComplete(openActionResult))
                },
                onCodeShared = {
                    viewModel.onNewAction(
                        QRCodeInfoUIAction.QRActionComplete(
                            context.shareTextToAnotherApp(it),
                        ),
                    )
                },
            ),
        cameraPermissionStatus = cameraPermissionState.status,
        uiState = uiState,
        largeScreen = largeScreen,
    )

    TemporaryMessage(data = uiState.temporaryMessage) {
        viewModel.onNewAction(action = QRCodeInfoUIAction.TmpMessageShown)
    }
}

fun NavController.navigateToScanQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(ScanQRCodeInfoRoute, navOptions)
}
