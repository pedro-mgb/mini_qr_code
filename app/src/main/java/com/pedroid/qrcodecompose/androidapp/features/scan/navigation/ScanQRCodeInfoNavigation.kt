package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pedroid.qrcodecompose.androidapp.core.presentation.launchPermissionRequestOrRun
import com.pedroid.qrcodecompose.androidapp.core.presentation.rememberPermissionState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeInfoUIAction
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeInfoUIState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeInfoScreen
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeInfoViewModel

const val SCAN_ROUTE = "SCAN_QR_CODE_ROUTE"

fun NavGraphBuilder.scanQRCodeInfoRoute(
    navigationListeners: ScanQRCodeInfoNavigationListeners,
    largeScreen: Boolean
) {
    composable(route = SCAN_ROUTE) {
        ScanCodeHome(navigationListeners, largeScreen, it.savedStateHandle)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ScanCodeHome(
    navigationListeners: ScanQRCodeInfoNavigationListeners,
    largeScreen: Boolean,
    savedStateHandle: SavedStateHandle,
    viewModel: ScanQRCodeInfoViewModel = hiltViewModel()
) {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA,
        onPermissionGranted = {
            navigationListeners.onGoScanQRCode()
        }
    )
    (savedStateHandle.get<String>(QR_CODE_SCANNED_KEY)).let {
        viewModel.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = it))
    }
    val uiState: QRCodeInfoUIState by viewModel.uiState.collectAsStateWithLifecycle()

    ScanQRCodeInfoScreen(
        onScanCodePressed = {
            cameraPermissionState.launchPermissionRequestOrRun {
                navigationListeners.onGoScanQRCode()
            }
        },
        actionListeners = ScannedQRCodeActionListeners(
            onCodeCopied = {
                // TODO implement
            },
            onCodeOpen = {
                // TODO implement
            },
            onCodeShared = {
                // TODO implement
            }
        ),
        cameraPermissionStatus = cameraPermissionState.status,
        uiState = uiState,
        largeScreen = largeScreen,
    )
}

fun NavController.navigateToScanQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(SCAN_ROUTE, navOptions)
}
