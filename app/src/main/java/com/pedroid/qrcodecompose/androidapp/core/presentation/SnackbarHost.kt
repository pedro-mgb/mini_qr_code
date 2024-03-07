package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppSnackbar
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppSnackbarVisuals

@Composable
fun QRAppSnackbarHost(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(snackbarHostState, modifier) { snackbarData ->
        val qrAppVisuals = snackbarData.visuals as? QRAppSnackbarVisuals
        if (qrAppVisuals == null) {
            throw IllegalStateException("The snackbar shown in QRAppSnackbarHost must gave QRAppSnackbarVisuals")
        } else {
            QRAppSnackbar(visuals = qrAppVisuals)
        }
    }
}
