package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

const val QR_CODE_SCANNED_KEY = "QRCodeXValue"

fun NavGraphBuilder.scanningFeatureNavigationRoutes(
    navController: NavController
) {
    scanQRCodeInfoRoute(
        navigationListeners = ScanQRCodeInfoNavigationListeners(
            onGoScanQRCode = {
                navController.navigateToScanQRCodeCamera()
            }
        )
    )
    scanQRCodeCameraRoute(
        navigationListeners = ScanQRCodeCameraNavigationListeners(
            onCodeScanned = {
                navController.popBackStack()
                navController.currentBackStackEntry
                    ?.savedStateHandle
                    ?.set(QR_CODE_SCANNED_KEY, it)
            },
            onBackInvoked = {
                navController.popBackStack()
            }
        )
    )
}