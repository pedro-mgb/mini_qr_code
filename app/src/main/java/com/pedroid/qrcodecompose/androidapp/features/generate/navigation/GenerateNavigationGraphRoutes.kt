package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

fun NavGraphBuilder.generateFeatureNavigationRoutes(navController: NavController) {
    generateQRCodeRoute()
}
