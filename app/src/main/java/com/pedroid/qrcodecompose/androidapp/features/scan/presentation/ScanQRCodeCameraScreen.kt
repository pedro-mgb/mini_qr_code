package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.pedroid.qrcode_compose_x.scan.QRCodeComposeXScanner
import com.pedroid.qrcode_compose_x.scan.QRCodeScanResult
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppToolbar

@Composable
fun ScanQRCodeCameraScreen(
    onQRCodeResult: (QRCodeScanResult) -> Unit,
    onBackInvoked: () -> Unit,
    largeScreen: Boolean = false,
) {
    val context = LocalContext.current
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA,
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // should return to home screen because we cannot scan with permission
        //  permission request to be done in home screen
        onBackInvoked()
        return
    }
    ScanQRCodeCameraContent(
        modifier = Modifier.fillMaxSize(),
        onBackInvoked = onBackInvoked,
        largeScreen = largeScreen,
    ) { modifier ->
        QRCodeComposeXScanner(
            modifier = modifier,
            onResult = onQRCodeResult,
            frameColor = Color.Green,
            frameVerticalPercent = 0.4f,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScanQRCodeCameraContent(
    modifier: Modifier = Modifier,
    onBackInvoked: () -> Unit,
    largeScreen: Boolean = false,
    cameraContent: @Composable (modifier: Modifier) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QRAppToolbar(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.scan_code_camera_toolbar_title,
            onNavigationIconClick = onBackInvoked,
        )
        Box(
            modifier = modifier,
            contentAlignment = if (largeScreen) Alignment.TopCenter else Alignment.BottomCenter,
        ) {
            cameraContent(Modifier.matchParentSize())
            InformativeLabel()
        }
    }
}

@Composable
private fun InformativeLabel() {
    Card(
        modifier =
            Modifier
                .fillMaxWidth(fraction = 0.8f)
                .padding(bottom = 20.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(id = R.string.scan_code_camera_instructions),
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
