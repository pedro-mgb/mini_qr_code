package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.Snackbar
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeScreen
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeUIAction
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeUIState
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeViewModel
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

const val GENERATE_ROUTE = "GENERATE_QR_CODE_ROUTE"

fun NavGraphBuilder.generateQRCodeRoute(largeScreen: Boolean = false) {
    composable(route = GENERATE_ROUTE) {
        GenerateQRCodeCoordinator(largeScreen = largeScreen)
    }
}

@Composable
private fun GenerateQRCodeCoordinator(
    largeScreen: Boolean,
    viewModel: GenerateQRCodeViewModel = hiltViewModel(),
) {
    val uiState: GenerateQRCodeUIState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentQRCodeBitmap: Bitmap? by remember { mutableStateOf(null) }

    GenerateQRCodeScreen(
        state = uiState.content,
        qrCodeUpdateListeners =
            GeneratedQRCodeUpdateListeners(
                onTextUpdated = {
                    viewModel.onNewAction(GenerateQRCodeUIAction.UpdateText(it))
                },
                onGeneratorResult = { result ->
                    when (result) {
                        is QRCodeGenerateResult.Generated -> {
                            currentQRCodeBitmap = result.bitmap
                        }

                        is QRCodeGenerateResult.Error -> {
                            viewModel.onNewAction(GenerateQRCodeUIAction.ErrorReceived(result.exception))
                        }
                    }
                },
            ),
        qrCodeActionListeners =
            GenerateQRCodeActionListeners(
                onImageSaveToFile = {
                    currentQRCodeBitmap?.let {
                        // todo implement action
                    }
                },
                onImageShare = {
                    currentQRCodeBitmap?.let {
                        // todo implement action
                    }
                },
                onImageCopyToClipboard = {
                    currentQRCodeBitmap?.let {
                        // todo implement action
                    }
                },
            ),
        largeScreen = largeScreen,
    )

    Snackbar(messageKey = uiState.errorMessageKey) {
        viewModel.onNewAction(action = GenerateQRCodeUIAction.ErrorShown)
    }
}

fun NavController.navigateToGenerateQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(GENERATE_ROUTE, navOptions)
}
