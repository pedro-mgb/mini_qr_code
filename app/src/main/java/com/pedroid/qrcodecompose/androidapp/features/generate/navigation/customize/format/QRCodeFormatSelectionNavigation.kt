package com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.QRCodeSelectFormatListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.customize.format.QRCodeFormatSelectionScreen
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

private const val ROUTE_START = "SELECT_CODE_FORMAT_ROUTE"
private const val ARGUMENT_KEY = "selectedFormat"
private const val SELECT_FORMAT_ROUTE = "$ROUTE_START/{$ARGUMENT_KEY}"

private val defaultFormat = QRCodeComposeXFormat.QR_CODE.name
private val navArguments =
    listOf(
        navArgument(ARGUMENT_KEY) {
            type = NavType.StringType
            defaultValue = defaultFormat
        },
    )

fun NavGraphBuilder.generateQRCodeSelectFormatRoute(
    navigationListeners: QRCodeSelectFormatListeners,
    largeScreen: Boolean = false,
) {
    composable(
        SELECT_FORMAT_ROUTE,
        navArguments,
    ) {
        QRCodeFormatSelectionCoordinator(
            navigationListeners = navigationListeners,
            selectedFormat = it.arguments?.getString(ARGUMENT_KEY),
            largeScreen = largeScreen,
        )
    }
}

@Composable
fun QRCodeFormatSelectionCoordinator(
    navigationListeners: QRCodeSelectFormatListeners,
    selectedFormat: String?,
    largeScreen: Boolean = false,
) {
    val qrcodeComposeXFormat =
        remember {
            QRCodeComposeXFormat.entries.firstOrNull {
                it.name == selectedFormat
            }
        }
    if (qrcodeComposeXFormat == null) {
        // if the argument arrives as null, most likely it is a result of some error in the code
        //  rather than crashing the app, we are silently closing the screen
        navigationListeners.onCancel()
    } else {
        QRCodeFormatSelectionScreen(
            selectedFormat = qrcodeComposeXFormat,
            actionListeners = navigationListeners,
            largeScreen = largeScreen,
        )
    }
}

fun NavController.navigateToQRCodeSelectFormat(
    navOptions: NavOptions? = null,
    format: QRCodeComposeXFormat,
) {
    this.navigate("$ROUTE_START/$format", navOptions)
}
