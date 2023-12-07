package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.pedroid.qrcode_compose_x.scan.QRCodeComposeXScanner
import com.pedroid.qrcode_compose_x.scan.QRCodeScanResult

@Composable
fun ScanQRCodeCameraScreen() {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        Text("Scan")
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO display fatal error and/or navigate back
            return
        }
        QRCodeComposeXScanner(
            modifier = Modifier.fillMaxSize(),
            onResult = { result ->
                if (result is QRCodeScanResult.Scanned) {
                    // TODO return result
                }
            }
        )
    }
}