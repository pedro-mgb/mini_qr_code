package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

const val QR_CODE_SCANNED_KEY = "QRCodeXValue"

fun NavGraphBuilder.scanningFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean,
) {
    scanQRCodeInfoRoute(
        navigationListeners =
            ScanQRCodeInfoNavigationListeners(
                onGoScanQRCode = {
                    navController.navigateToScanQRCodeCamera()
                },
            ),
        largeScreen = largeScreen,
    )
    scanQRCodeCameraRoute(
        navigationListeners =
            ScanQRCodeCameraNavigationListeners(
                onCodeScanned = {
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(QR_CODE_SCANNED_KEY, it)
                },
                onBackInvoked = {
                    navController.popBackStack()
                },
            ),
        largeScreen = largeScreen,
    )
}
