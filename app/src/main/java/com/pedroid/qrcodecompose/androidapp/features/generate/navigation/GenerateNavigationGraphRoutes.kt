package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.QRCodeSelectFormatListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.generateQRCodeSelectFormatRoute
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.navigateToQRCodeSelectFormat
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.expand.expandGeneratedQRCodeRoute
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.expand.navigateToExpandGeneratedQRCode

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
                onExpand = {
                    navController.navigateToExpandGeneratedQRCode(arguments = it)
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
    expandGeneratedQRCodeRoute(
        navigationListeners =
            ExpandQRCodeNavigationListeners(
                goBack = {
                    navController.popBackStack()
                },
            ),
        sharedTransitionScope,
    )
}
