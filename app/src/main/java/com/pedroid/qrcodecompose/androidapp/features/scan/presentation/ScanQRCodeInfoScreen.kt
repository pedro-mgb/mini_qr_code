package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.ContentCopy
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.ScannedQRCodeActionListeners

// region screen composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQRCodeInfoScreen(
    onScanCodePressed: () -> Unit,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
    cameraPermissionStatus: PermissionStatus,
    uiState: QRCodeInfoUIState = QRCodeInfoUIState(),
    largeScreen: Boolean = false,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState.content) {
            is QRCodeInfoContentUIState.CodeScanned -> {
                QRCodeReadContent(
                    modifier = Modifier.fillMaxWidth(),
                    actionListeners = actionListeners,
                    qrCode = uiState.content.qrCode,
                    largeScreen = largeScreen,
                )
            }

            else -> {
                InitialInfoHeader()
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        CameraPermissionContent(cameraPermissionStatus = cameraPermissionStatus)
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = onScanCodePressed
        ) {
            Text(
                text = if (uiState.content is QRCodeInfoContentUIState.CodeScanned) {
                    stringResource(id = R.string.scan_another_code_action_button)
                } else {
                    stringResource(id = R.string.scan_code_action_button)
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
private fun QRCodeReadContent(
    modifier: Modifier = Modifier,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
    qrCode: String,
    largeScreen: Boolean,
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            text = stringResource(id = R.string.scan_code_read_label),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                textDecoration = TextDecoration.Underline,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Divider(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.primaryContainer
        )
        if (largeScreen) {
            QRCodeContentLargeScreen(qrCode = qrCode, actionListeners = actionListeners)
        } else {
            QRCodeContentPortrait(qrCode = qrCode, actionListeners = actionListeners)
        }
    }
}

@Composable
private fun QRCodeContentLargeScreen(
    modifier: Modifier = Modifier,
    qrCode: String,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners()
) {
    Row(modifier = modifier) {
        QRCodeDataContent(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 200.dp, max = 500.dp),
            qrCode = qrCode,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(20.dp))
        Card {
            Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 10.dp)) {
                QRCodeActionButtons(qrCode = qrCode, actionListeners = actionListeners)
            }
        }
    }
}

@Composable
private fun QRCodeContentPortrait(
    modifier: Modifier = Modifier,
    qrCode: String,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners()
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        QRCodeDataContent(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 200.dp),
            qrCode = qrCode
        )
        Spacer(modifier = Modifier.size(10.dp))
        Card {
            Row(
                modifier = Modifier.padding(vertical = 5.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                QRCodeActionButtons(qrCode = qrCode, actionListeners = actionListeners)
            }
        }
    }
}

@Composable
private fun QRCodeDataContent(
    modifier: Modifier = Modifier,
    qrCode: String,
    textAlign: TextAlign = TextAlign.Start,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            text = qrCode,
            textAlign = textAlign,
        )
    }
}

@Composable
private fun QRCodeActionButtons(
    qrCode: String,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners()
) {
    IconButton(onClick = { actionListeners.onCodeOpen(qrCode) }) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Filled.ExitToApp,
            contentDescription = stringResource(id = R.string.action_open)
        )
    }
    Spacer(modifier = Modifier.size(20.dp))
    IconButton(onClick = { actionListeners.onCodeShared(qrCode) }) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.action_share)
        )
    }
    Spacer(modifier = Modifier.size(20.dp))
    IconButton(onClick = { actionListeners.onCodeCopied(qrCode) }) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Outlined.ContentCopy,
            contentDescription = stringResource(id = R.string.action_copy)
        )
    }
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
// endregion screen composables

// region screen previews
@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun ScanQRCodeInfoScreenPermissionGrantedPreview() {
    BaseQRCodeAppPreview {
        ScanQRCodeInfoScreen(
            onScanCodePressed = { },
            cameraPermissionStatus = PermissionStatus.Granted,
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
            cameraPermissionStatus = PermissionStatus.Denied(shouldShowRationale = true),
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
            uiState = QRCodeInfoUIState(QRCodeInfoContentUIState.CodeScanned(qrCode = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")),
            largeScreen = false
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun ScanQRCodeInfoScreenWithCodeReadTabletPreview() {
    BaseQRCodeAppPreview {
        ScanQRCodeInfoScreen(
            onScanCodePressed = { },
            cameraPermissionStatus = PermissionStatus.Granted,
            uiState = QRCodeInfoUIState(QRCodeInfoContentUIState.CodeScanned("some_qr_code")),
            largeScreen = true
        )
    }
}
// endregion screen previews