package com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.ScanFromFileActionListeners

// region composables
@Composable
fun ScanQRCodeFromFileScreen(
    uiState: QRCodeFromFileUIState,
    scanFromFileActionListeners: ScanFromFileActionListeners,
    largeScreen: Boolean = false,
) {
    // TODO finish implementation
    if (uiState is QRCodeFromFileUIState.Error) {
    } else {
        LoadingFromFile()
    }
}

@Composable
private fun LoadingFromFile() {
    CircularProgressIndicator()
}
// endregion composables
