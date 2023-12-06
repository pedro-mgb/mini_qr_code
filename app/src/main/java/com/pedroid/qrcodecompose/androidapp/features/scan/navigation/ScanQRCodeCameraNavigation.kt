package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeCameraScreen

const val SCAN_CAMERA_READER_ROUTE = "SCAN_QR_CODE_CAMERA_READER_ROUTE"

fun NavGraphBuilder.scanQRCodeCameraRoute() {
    composable(route = SCAN_CAMERA_READER_ROUTE) {
        ScanQRCodeCameraScreen()
    }
}

fun NavController.navigateToScanQRCodeCamera(navOptions: NavOptions? = null) {
    this.navigate(SCAN_CAMERA_READER_ROUTE, navOptions)
}