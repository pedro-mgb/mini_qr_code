package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

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
    scanQRCodeCameraRoute()
}