package com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pedroid.qrcodecompose.androidapp.core.navigation.ofQRCodeComposeXFormat
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.QRCodeSelectFormatListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.customize.format.QRCodeFormatSelectionScreen
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
@Parcelize
data class SelectFormatRoute(
    val currentFormat: QRCodeComposeXFormat,
) : Parcelable

fun NavGraphBuilder.generateQRCodeSelectFormatRoute(
    navigationListeners: QRCodeSelectFormatListeners,
    largeScreen: Boolean = false,
) {
    composable<SelectFormatRoute>(
        typeMap = mapOf(typeOf<QRCodeComposeXFormat>() to NavType.ofQRCodeComposeXFormat()),
    ) {
        val route = it.toRoute<SelectFormatRoute>()
        QRCodeFormatSelectionCoordinator(
            navigationListeners = navigationListeners,
            selectedFormat = route.currentFormat,
            largeScreen = largeScreen,
        )
    }
}

@Composable
fun QRCodeFormatSelectionCoordinator(
    navigationListeners: QRCodeSelectFormatListeners,
    selectedFormat: QRCodeComposeXFormat,
    largeScreen: Boolean = false,
) {
    QRCodeFormatSelectionScreen(
        selectedFormat = selectedFormat,
        actionListeners = navigationListeners,
        largeScreen = largeScreen,
    )
}

fun NavController.navigateToQRCodeSelectFormat(
    navOptions: NavOptions? = null,
    format: QRCodeComposeXFormat,
) {
    this.navigate(SelectFormatRoute(format), navOptions)
}
