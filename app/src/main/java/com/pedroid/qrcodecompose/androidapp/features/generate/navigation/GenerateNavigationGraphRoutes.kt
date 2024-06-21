package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.QRCodeSelectFormatListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.generateQRCodeSelectFormatRoute
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.navigateToQRCodeSelectFormat

const val CODE_FORMAT_KEY = "QRCodeMiniFormat"

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.generateFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
) {
    generateQRCodeRoute(
        navigationListeners =
            GenerateQRCodeHomeNavigationListeners(
                onCustomize = {
                    navController.navigateToQRCodeSelectFormat(format = it.format)
                },
            ),
        largeScreen = largeScreen,
        sharedTransitionScope,
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
