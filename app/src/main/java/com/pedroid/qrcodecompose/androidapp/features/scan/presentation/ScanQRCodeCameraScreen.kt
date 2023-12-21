package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.pedroid.qrcode_compose_x.scan.QRCodeComposeXScanner
import com.pedroid.qrcode_compose_x.scan.QRCodeScanResult
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppToolbar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQRCodeCameraScreen(
    onQRCodeResult: (QRCodeScanResult) -> Unit,
    onBackInvoked: () -> Unit
) {
    val context = LocalContext.current
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // TODO possible display fatal error and/or navigate back
        return
    }
    Column(modifier = Modifier.fillMaxSize()) {
        // TODO update UI with other useful information
        QRAppToolbar(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.scan_code_camera_toolbar_title,
            onNavigationIconClick = onBackInvoked
        )
        QRCodeComposeXScanner(
            modifier = Modifier.fillMaxSize(),
            onResult = onQRCodeResult,
            frameColor = Color.Green,
            frameVerticalPercent = 0.4f
        )
    }
}