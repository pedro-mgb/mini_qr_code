package com.pedroid.qrcodecompose.androidapp.features.scan.navigation.expand

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.expand.presentation.ExpandQRCodeScreen
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

// note this class is a copy of ExpandQRCodeArguments, but was not able to put it as an attribute inside this route class, as it was making navigation crash
//  possibly some bug with the type-safety of navigation, but didn't bother to investigate further, as it's fairly minor to have it like this
@Serializable
data class ExpandScannedQRCodeRoute(
    val label: String,
    val code: String,
    val format: QRCodeComposeXFormat,
)

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.expandScannedQRCodeRoute(
    navigationListeners: ExpandQRCodeNavigationListeners,
    sharedTransitionScope: SharedTransitionScope,
) {
    composable<ExpandScannedQRCodeRoute>(
        typeMap = mapOf(typeOf<QRCodeComposeXFormat>() to NavType.EnumType(QRCodeComposeXFormat::class.java)),
    ) {
        val route = it.toRoute<ExpandScannedQRCodeRoute>()
        ExpandQRCodeScreen(
            ExpandQRCodeArguments(
                route.label,
                route.code,
                route.format,
            ),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = this@composable,
            navigationListeners = navigationListeners,
        )
    }
}

fun NavController.navigateToExpandScannedQRCode(
    navOptions: NavOptions? = null,
    arguments: ExpandQRCodeArguments,
) {
    this.navigate(
        ExpandScannedQRCodeRoute(
            label = arguments.key,
            code = arguments.code,
            format = arguments.format,
        ),
        navOptions,
    )
}
