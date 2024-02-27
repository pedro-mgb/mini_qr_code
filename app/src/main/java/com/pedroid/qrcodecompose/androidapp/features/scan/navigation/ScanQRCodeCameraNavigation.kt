package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeCameraUIAction
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeCameraUIState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeCameraScreen
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeCameraViewModel

const val SCAN_CAMERA_READER_ROUTE = "SCAN_QR_CODE_CAMERA_READER_ROUTE"

fun NavGraphBuilder.scanQRCodeCameraRoute(
    navigationListeners: ScanQRCodeCameraNavigationListeners,
    largeScreen: Boolean,
) {
    composable(route = SCAN_CAMERA_READER_ROUTE) {
        ScanQRCodeCameraCoordinator(navigationListeners, largeScreen)
    }
}

@Composable
private fun ScanQRCodeCameraCoordinator(
    navigationListeners: ScanQRCodeCameraNavigationListeners,
    largeScreen: Boolean,
    viewModel: ScanQRCodeCameraViewModel = hiltViewModel()
) {
    val uiState: QRCodeCameraUIState by viewModel.uiState.collectAsStateWithLifecycle()
    uiState.apply {
        when (this) {
            is QRCodeCameraUIState.ScanComplete -> {
                navigationListeners.onCodeScanned(qrCode)
            }

            is QRCodeCameraUIState.Idle -> {
                // nothing to do here
            }
        }
    }
    ScanQRCodeCameraScreen(
        onQRCodeResult = {
            viewModel.onNewAction(QRCodeCameraUIAction.ResultUpdate(it))
        },
        onBackInvoked = navigationListeners.onBackInvoked,
        largeScreen = largeScreen,
    )
}

fun NavController.navigateToScanQRCodeCamera(
    navOptions: NavOptions = androidx.navigation.navOptions {
        launchSingleTop = true
    }
) {
    this.navigate(SCAN_CAMERA_READER_ROUTE, navOptions)
}