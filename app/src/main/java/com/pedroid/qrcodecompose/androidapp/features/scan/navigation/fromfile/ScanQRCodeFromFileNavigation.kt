package com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScanSource
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.ScanQRCodeCameraNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile.QRCodeFromFileUIAction
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile.QRCodeFromFileUIState
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile.ScanQRCodeFromFileScreen
import com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile.ScanQRCodeFromFileViewModel
import com.pedroid.qrcodecomposelib.scan.QRCodeFileAnalyzer
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import com.pedroid.qrcodecomposelibmlkit.MLKitImageAnalyzer

const val SCAN_FILE_READER_ROUTE = "SCAN_QR_CODE_FILE_READER_ROUTE"
private val imagesOnlyPicker =
    PickVisualMediaRequest(
        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
    )

fun NavGraphBuilder.scanQRCodeFromFileRoute(
    navigationListeners: ScanQRCodeCameraNavigationListeners,
    largeScreen: Boolean,
) {
    composable(route = SCAN_FILE_READER_ROUTE) {
        ScanQRCodeFromFileCoordinator(navigationListeners, largeScreen)
    }
}

// region coordinator
@Composable
fun ScanQRCodeFromFileCoordinator(
    navigationListeners: ScanQRCodeCameraNavigationListeners,
    largeScreen: Boolean,
    viewModel: ScanQRCodeFromFileViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val fileLauncher =
        rememberImageFileScannerActivityResult(
            context = context,
            onResult = { result ->
                viewModel.onNewAction(QRCodeFromFileUIAction.ScanResult(result))
            },
        )
    val uiState: QRCodeFromFileUIState by viewModel.uiState.collectAsStateWithLifecycle()

    ScanQRCodeFromFileScreen(
        uiState = uiState,
        scanFromFileActionListeners =
            ScanFromFileActionListeners(
                onRetryScan = {
                    fileLauncher.launch(imagesOnlyPicker)
                },
                onCancel = {
                    navigationListeners.onBackInvoked()
                },
            ),
        largeScreen = largeScreen,
    )

    // start the screen with opening photo picker
    LaunchedEffect(key1 = uiState) {
        if (uiState is QRCodeFromFileUIState.Init) {
            fileLauncher.launch(imagesOnlyPicker)
            viewModel.onNewAction(QRCodeFromFileUIAction.StartFileSelection)
        }
    }

    // navigation events depending on ui state
    LaunchedEffect(key1 = uiState, key2 = navigationListeners) {
        performNavigation(uiState, navigationListeners)
    }
}

private fun performNavigation(
    uiState: QRCodeFromFileUIState,
    navigationListeners: ScanQRCodeCameraNavigationListeners,
) {
    when (uiState) {
        QRCodeFromFileUIState.Cancelled -> {
            navigationListeners.onBackInvoked()
        }

        is QRCodeFromFileUIState.Success -> {
            navigationListeners.onCodeScanned(
                ScannedCode(
                    data = uiState.qrCode,
                    format = uiState.format,
                    source = ScanSource.IMAGE_FILE,
                ),
            )
        }

        else -> {
            // no other navigation to do
        }
    }
}

@Composable
private fun rememberImageFileScannerActivityResult(
    context: Context,
    onResult: (QRCodeScanResult) -> Unit,
    analyzer: QRCodeFileAnalyzer = rememberMLKitImageFileAnalyzer(onResult),
) = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
    if (uri != null) {
        analyzer.analyze(context, uri)
    } else {
        onResult(QRCodeScanResult.Cancelled)
    }
}

@OptIn(ExperimentalGetImage::class)
private fun rememberMLKitImageFileAnalyzer(onResult: (QRCodeScanResult) -> Unit): QRCodeFileAnalyzer = MLKitImageAnalyzer(onResult)
// endregion coordinator

fun NavController.navigateToScanQRCodeFromFile(
    navOptions: NavOptions =
        androidx.navigation.navOptions {
            launchSingleTop = true
        },
) {
    this.navigate(SCAN_FILE_READER_ROUTE, navOptions)
}
