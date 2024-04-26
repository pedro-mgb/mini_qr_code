package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.QRCodeSelectFormatListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.generateQRCodeSelectFormatRoute
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.navigateToQRCodeSelectFormat

const val CODE_FORMAT_KEY = "QRCodeMiniFormat"

fun NavGraphBuilder.generateFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean = false,
) {
    generateQRCodeRoute(
        navigationListeners =
            GenerateQRCodeHomeNavigationListeners(
                onCustomize = {
                    navController.navigateToQRCodeSelectFormat(format = it.format)
                },
            ),
        largeScreen = largeScreen,
    )
    generateQRCodeSelectFormatRoute(
        navigationListeners =
            QRCodeSelectFormatListeners(
                onSelectFormat = {
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(CODE_FORMAT_KEY, it)
                },
                onCancel = {
                    navController.popBackStack()
                },
            ),
    )
}
