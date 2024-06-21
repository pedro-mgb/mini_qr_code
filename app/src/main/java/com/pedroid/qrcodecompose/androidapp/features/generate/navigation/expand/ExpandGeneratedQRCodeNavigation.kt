package com.pedroid.qrcodecompose.androidapp.features.generate.navigation.expand

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pedroid.qrcodecompose.androidapp.core.navigation.ofQRCodeComposeXFormat
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ofExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.presentation.ExpandQRCodeScreen
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
data class ExpandGeneratedQRCodeRoute(
    val arguments: ExpandQRCodeArguments,
)

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.expandGeneratedQRCodeRoute(
    navigationListeners: ExpandQRCodeNavigationListeners,
    sharedTransitionScope: SharedTransitionScope,
) {
    composable<ExpandGeneratedQRCodeRoute>(
        typeMap =
            mapOf(
                typeOf<ExpandQRCodeArguments>() to NavType.ofExpandQRCodeArguments(),
                typeOf<QRCodeComposeXFormat>() to NavType.ofQRCodeComposeXFormat(),
            ),
    ) {
        val route = it.toRoute<ExpandGeneratedQRCodeRoute>()
        ExpandQRCodeScreen(
            route.arguments,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this@composable,
            navigationListeners = navigationListeners,
        )
    }
}

fun NavController.navigateToExpandGeneratedQRCode(
    navOptions: NavOptions? = null,
    arguments: ExpandQRCodeArguments,
) {
    this.navigate(ExpandGeneratedQRCodeRoute(arguments), navOptions)
}
