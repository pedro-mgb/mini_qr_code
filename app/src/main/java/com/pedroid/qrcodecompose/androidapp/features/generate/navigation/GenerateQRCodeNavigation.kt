package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeScreen

const val GENERATE_ROUTE = "GENERATE_QR_CODE_ROUTE"

fun NavGraphBuilder.generateQRCodeRoute() {
    composable(route = GENERATE_ROUTE) {
        GenerateQRCodeScreen()
    }
}

fun NavController.navigateToGenerateQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(GENERATE_ROUTE, navOptions)
}