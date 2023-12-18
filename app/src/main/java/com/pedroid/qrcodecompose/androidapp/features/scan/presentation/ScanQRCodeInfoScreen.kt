package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQRCodeInfoScreen(
    onScanCodePressed: () -> Unit,
    cameraPermissionStatus: PermissionStatus,
    scannedCode: String = ""
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (scannedCode.isEmpty()) {
            InitialInfoHeader()
        } else {
            QRCodeDataContent(qrCode = scannedCode)
        }
        Spacer(modifier = Modifier.height(20.dp))
        CameraPermissionContent(cameraPermissionStatus = cameraPermissionStatus)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = onScanCodePressed
        ) {
            Text(
                text = if (scannedCode.isEmpty()) {
                    stringResource(id = R.string.scan_code_action_button)
                } else {
                    stringResource(id = R.string.scan_another_code_action_button)
                }
            )
        }
    }
}

@Composable
private fun InitialInfoHeader() {
    Text(
        modifier = Modifier.fillMaxWidth(fraction = 0.8f),
        text = stringResource(
            id = R.string.scan_code_header,
            stringResource(id = R.string.scan_code_action_button)
        ),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun QRCodeDataContent(
    qrCode: String
) {
    Text(qrCode)
}

@ExperimentalPermissionsApi
@Composable
private fun CameraPermissionContent(
    cameraPermissionStatus: PermissionStatus
) {
    when (cameraPermissionStatus) {
        PermissionStatus.Granted -> {
            // nothing to show here, as permission has already been granted
            Spacer(modifier = Modifier.height(20.dp))
        }

        is PermissionStatus.Denied -> {
            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                text = stringResource(
                    id = if (cameraPermissionStatus.shouldShowRationale) {
                        R.string.scan_code_permission_denied_info
                    } else {
                        R.string.scan_code_permission_info
                    }
                ),
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun ScanQRCodeInfoScreenPermissionGrantedPreview() {
    BaseQRCodeAppPreview {
        ScanQRCodeInfoScreen(
            onScanCodePressed = { },
            cameraPermissionStatus = PermissionStatus.Granted
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun ScanQRCodeInfoScreenPermissionDeniedPreview() {
    BaseQRCodeAppPreview {
        ScanQRCodeInfoScreen(
            onScanCodePressed = { },
            cameraPermissionStatus = PermissionStatus.Denied(shouldShowRationale = true)
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun ScanQRCodeInfoScreenWithCodeReadPreview() {
    BaseQRCodeAppPreview {
        ScanQRCodeInfoScreen(
            onScanCodePressed = { },
            cameraPermissionStatus = PermissionStatus.Granted,
            scannedCode = "some_qr_code"
        )
    }
}