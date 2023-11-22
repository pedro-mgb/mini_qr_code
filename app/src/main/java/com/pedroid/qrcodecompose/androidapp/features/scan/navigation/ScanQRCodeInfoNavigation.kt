package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.ScanQRCodeInfoScreen

const val SCAN_ROUTE = "SCAN_QR_CODE_ROUTE"

fun NavGraphBuilder.scanQRCodeInfoRoute() {
    composable(route = SCAN_ROUTE) {
        ScanQRCodeInfoScreen()
    }
}

fun NavController.navigateToScanQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(SCAN_ROUTE, navOptions)
}