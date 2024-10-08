package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import android.content.Context
import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.IMAGE_MIME_TYPE
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.copyImageToClipboard
import com.pedroid.qrcodecompose.androidapp.core.presentation.saveBitmap
import com.pedroid.qrcodecompose.androidapp.core.presentation.shareImageToAnotherApp
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeCustomizationOptions
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeScreen
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeUIAction
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeUIState
import com.pedroid.qrcodecompose.androidapp.features.generate.presentation.GenerateQRCodeViewModel
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult
import kotlinx.serialization.Serializable

@Serializable
data object GenerateQRCodeHomeRoute

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.generateQRCodeRoute(
    navigationListeners: GenerateQRCodeHomeNavigationListeners,
    largeScreen: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
) {
    composable<GenerateQRCodeHomeRoute> {
        GenerateQRCodeCoordinator(
            navigationListeners,
            largeScreen,
            it.savedStateHandle,
            sharedTransitionScope,
            animatedVisibilityScope = this@composable,
        )
    }
}

@Composable
@ExperimentalSharedTransitionApi
private fun GenerateQRCodeCoordinator(
    navigationListeners: GenerateQRCodeHomeNavigationListeners,
    largeScreen: Boolean,
    savedStateHandle: SavedStateHandle,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: GenerateQRCodeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val uiState: GenerateQRCodeUIState by viewModel.uiState.collectAsStateWithLifecycle()
    (savedStateHandle.get<QRCodeComposeXFormat>(CODE_FORMAT_KEY))?.let {
        LaunchedEffect(key1 = it) {
            viewModel.onNewAction(GenerateQRCodeUIAction.Customize(options = QRCodeCustomizationOptions(it)))
        }
    }
    var currentQRCodeBitmap: Bitmap? by remember { mutableStateOf(null) }
    val saveImageLauncher =
        rememberSaveBitmapActivityResult(
            context = context,
            retrieveBitmap = { currentQRCodeBitmap },
            onResult = {
                viewModel.onNewAction(
                    GenerateQRCodeUIAction.QRActionComplete(
                        QRAppActions.SaveToFile(it),
                    ),
                )
            },
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
                onCustomize = {
                    navigationListeners.onCustomize(QRCodeCustomizationOptions((uiState.content.generating.format)))
                },
                onImageSaveToFile = {
                    saveImageLauncher.launch(context.getNameFromFormat(uiState))
                },
                onImageShare = {
                    val result =
                        context.shareImageToAnotherApp(
                            content = currentQRCodeBitmap,
                            shareTitle = context.getNameFromFormat(uiState),
                        )
                    viewModel.onNewAction(GenerateQRCodeUIAction.QRActionComplete(result))
                },
                onImageCopyToClipboard = {
                    val result = context.copyImageToClipboard(currentQRCodeBitmap)
                    viewModel.onNewAction(
                        GenerateQRCodeUIAction.QRActionComplete(result),
                    )
                },
                onExpand = navigationListeners.onExpand,
            ),
        largeScreen = largeScreen,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    )

    TemporaryMessage(data = uiState.temporaryMessage) {
        viewModel.onNewAction(action = GenerateQRCodeUIAction.TmpMessageShown)
    }
}

@Composable
private fun rememberSaveBitmapActivityResult(
    context: Context,
    retrieveBitmap: () -> Bitmap?,
    onResult: (ActionStatus) -> Unit,
) = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument(IMAGE_MIME_TYPE)) {
    it?.let { uri ->
        val bitmapSaved = uri.saveBitmap(context, retrieveBitmap())
        if (bitmapSaved) {
            onResult(ActionStatus.SUCCESS)
        } else {
            onResult(ActionStatus.ERROR_FILE)
        }
    }
}

private fun Context.getNameFromFormat(uiState: GenerateQRCodeUIState): String =
    this.getString(uiState.content.generating.format.titleStringId)

fun NavController.navigateToGenerateQRCodeInfo(navOptions: NavOptions? = null) {
    this.navigate(GenerateQRCodeHomeRoute, navOptions)
}
