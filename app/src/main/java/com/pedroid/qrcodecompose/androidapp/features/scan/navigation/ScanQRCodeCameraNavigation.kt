package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.shortPhoneVibration
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScanSource
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeCameraUIAction
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.QRCodeCameraUIState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeCameraScreen
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeCameraViewModel
import kotlinx.serialization.Serializable

@Serializable
data object ScanCameraReaderRoute

fun NavGraphBuilder.scanQRCodeCameraRoute(
    navigationListeners: ScanQRCodeCameraNavigationListeners,
    largeScreen: Boolean,
) {
    composable<ScanCameraReaderRoute> {
        ScanQRCodeCameraCoordinator(navigationListeners, largeScreen)
    }
}

@Composable
private fun ScanQRCodeCameraCoordinator(
    navigationListeners: ScanQRCodeCameraNavigationListeners,
    largeScreen: Boolean,
    viewModel: ScanQRCodeCameraViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState: QRCodeCameraUIState by viewModel.uiState.collectAsStateWithLifecycle()
    uiState.apply {
        when (this) {
            is QRCodeCameraUIState.ScanComplete -> {
                LaunchedEffect(key1 = this) {
                    if (vibrate) {
                        context.shortPhoneVibration()
                    }
                }
                navigationListeners.onCodeScanned(
                    ScannedCode(
                        data = this.qrCode,
                        format = this.format,
                        source = ScanSource.CAMERA,
                    ),
                )
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
    navOptions: NavOptions =
        androidx.navigation.navOptions {
            launchSingleTop = true
        },
) {
    this.navigate(ScanCameraReaderRoute, navOptions)
}
