package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.IMAGE_MIME_TYPE
import com.pedroid.qrcodecompose.androidapp.core.presentation.Snackbar
import com.pedroid.qrcodecompose.androidapp.core.presentation.saveBitmap
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
    val context = LocalContext.current
    val uiState: GenerateQRCodeUIState by viewModel.uiState.collectAsStateWithLifecycle()
    var currentQRCodeBitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(IMAGE_MIME_TYPE)) {
            it.let { uri ->
                val bitmapSaved = uri.saveBitmap(context, currentQRCodeBitmap)
                if (bitmapSaved) {
                    Toast.makeText(
                        context,
                        "",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    // TODO show error
                }
            }
        }

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
                    launcher.launch("QR Code")
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
