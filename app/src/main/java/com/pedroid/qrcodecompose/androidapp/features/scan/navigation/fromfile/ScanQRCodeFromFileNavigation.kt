package com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.ScanQRCodeCameraNavigationListeners
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
) {
    val context = LocalContext.current
    val fileLauncher =
        rememberImageFileScannerActivityResult(
            context = context,
            onResult = { result ->
                // TODO handle result
            },
        )
    LaunchedEffect(key1 = navigationListeners) {
        fileLauncher.launch(imagesOnlyPicker)
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
