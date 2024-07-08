package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.expand.expandScannedQRCodeRoute
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.expand.navigateToExpandScannedQRCode
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.navigateToScanQRCodeFromFile
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.scanQRCodeFromFileRoute

const val QR_CODE_SCANNED_KEY = "QRCodeMiniValue"

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.scanningFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean,
    sharedTransitionScope: SharedTransitionScope,
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
                onExpand = {
                    navController.navigateToExpandScannedQRCode(arguments = it)
                },
            ),
        largeScreen = largeScreen,
        sharedTransitionScope,
    )
    scanQRCodeCameraRoute(
        navigationListeners = navController.createScanCodeDataNavigationListeners(),
        largeScreen = largeScreen,
    )
    scanQRCodeFromFileRoute(
        navigationListeners = navController.createScanCodeDataNavigationListeners(),
        largeScreen = largeScreen,
    )
    expandScannedQRCodeRoute(
        sharedTransitionScope = sharedTransitionScope,
        navigationListeners =
            ExpandQRCodeNavigationListeners(
                goBack = {
                    navController.popBackStack()
                },
            ),
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
