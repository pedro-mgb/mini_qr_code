package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
            text = stringResource(
                id = R.string.scan_code_header,
                stringResource(id = R.string.scan_code_action_button)
            ),
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(20.dp))
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
        Button(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = onScanCodePressed
        ) {
            Text(text = stringResource(id = R.string.scan_code_action_button))
        }
    }
}

@Preview
@Composable
fun ScanQRCodeInfoScreenPreview() {
    BaseQRCodeAppPreview {

    }
}