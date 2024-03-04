package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import android.content.Context
import android.graphics.Bitmap
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
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.IMAGE_MIME_TYPE
import com.pedroid.qrcodecompose.androidapp.core.presentation.Snackbar
import com.pedroid.qrcodecompose.androidapp.core.presentation.copyImageToClipboard
import com.pedroid.qrcodecompose.androidapp.core.presentation.saveBitmap
import com.pedroid.qrcodecompose.androidapp.core.presentation.shareImageToAnotherApp
import com.pedroid.qrcodecompose.androidapp.core.presentation.showToast
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
    val saveImageLauncher =
        rememberSaveBitmapActivityResult(
            context = context,
            retrieveBitmap = { currentQRCodeBitmap },
            onErrorSaving = { viewModel.onNewAction(GenerateQRCodeUIAction.ErrorSavingToFile) },
        )

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
                            viewModel.onNewAction(GenerateQRCodeUIAction.GenerateErrorReceived(result.exception))
                        }
                    }
                },
            ),
        qrCodeActionListeners =
            GenerateQRCodeActionListeners(
                onImageSaveToFile = {
                    saveImageLauncher.launch("QR Code")
                },
                onImageShare = {
                    val result = context.shareImageToAnotherApp(currentQRCodeBitmap, "QR Code")
                    viewModel.onNewAction(GenerateQRCodeUIAction.AppStarted(result))
                },
                onImageCopyToClipboard = {
                    val copySuccess = context.copyImageToClipboard(currentQRCodeBitmap)
                    if (!copySuccess) {
                        viewModel.onNewAction(GenerateQRCodeUIAction.ErrorSavingToFile)
                    } else {
                        context.showToast(R.string.code_copied_success)
                    }
                },
            ),
        largeScreen = largeScreen,
    )

    Snackbar(messageKey = uiState.errorMessageKey) {
        viewModel.onNewAction(action = GenerateQRCodeUIAction.ErrorShown)
    }
}

@Composable
private fun rememberSaveBitmapActivityResult(
    context: Context,
    retrieveBitmap: () -> Bitmap?,
    onErrorSaving: () -> Unit,
) = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(IMAGE_MIME_TYPE)) {
    it.let { uri ->
        val bitmapSaved = uri.saveBitmap(context, retrieveBitmap())
        if (bitmapSaved) {
            context.showToast(R.string.code_saved_to_file_success)
        } else {
            onErrorSaving()
        }
    }
}

fun NavController.navigateToGenerateQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(GENERATE_ROUTE, navOptions)
}
