package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.navigateToScanQRCodeFromFile
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.scanQRCodeFromFileRoute

const val QR_CODE_SCANNED_KEY = "QRCodeXValue"

fun NavGraphBuilder.scanningFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean,
) {
    scanQRCodeInfoRoute(
        navigationListeners =
            ScanQRCodeInfoNavigationListeners(
                onGoScanQRCodeWithCamera = {
                    navController.navigateToScanQRCodeCamera()
                },
                onGoScanQRCodeFromFile = {
                    navController.navigateToScanQRCodeFromFile()
                },
            ),
        largeScreen = largeScreen,
    )
    scanQRCodeCameraRoute(
        navigationListeners = navController.createScanCodeDataNavigationListeners(),
        largeScreen = largeScreen,
    )
    scanQRCodeFromFileRoute(
        navigationListeners = navController.createScanCodeDataNavigationListeners(),
        largeScreen = largeScreen,
    )
}

private fun NavController.createScanCodeDataNavigationListeners(): ScanQRCodeCameraNavigationListeners =
    ScanQRCodeCameraNavigationListeners(
        onCodeScanned = {
            this.popBackStack()
            this.currentBackStackEntry
                ?.savedStateHandle
                ?.set(QR_CODE_SCANNED_KEY, it)
        },
        onBackInvoked = {
            this.popBackStack()
        },
    )
