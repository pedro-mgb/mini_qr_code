package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import android.Manifest
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.pedroid.qrcodecompose.androidapp.core.presentation.launchPermissionRequestOrRun
import com.pedroid.qrcodecompose.androidapp.core.presentation.rememberPermissionState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeInfoScreen

const val SCAN_ROUTE = "SCAN_QR_CODE_ROUTE"

@OptIn(ExperimentalPermissionsApi::class)
fun NavGraphBuilder.scanQRCodeInfoRoute(
    navigationListeners: ScanQRCodeInfoNavigationListeners
) {
    composable(route = SCAN_ROUTE) {
        val cameraPermissionState = rememberPermissionState(
            permission = Manifest.permission.CAMERA,
            onPermissionGranted = {
                navigationListeners.onGoScanQRCode()
            }
        )
        val scannedCode: String = it.savedStateHandle.get<String>(QR_CODE_SCANNED_KEY) ?: ""

        ScanQRCodeInfoScreen(
            onScanCodePressed = {
                cameraPermissionState.launchPermissionRequestOrRun {
                    navigationListeners.onGoScanQRCode()
                }
            },
            cameraPermissionStatus = cameraPermissionState.status,
            scannedCode = scannedCode,
        )
    }
}

fun NavController.navigateToScanQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(SCAN_ROUTE, navOptions)
}
