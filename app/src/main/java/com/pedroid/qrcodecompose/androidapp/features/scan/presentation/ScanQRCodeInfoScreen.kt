package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.QRCodeTextContent
import com.pedroid.qrcodecompose.androidapp.core.presentation.getWindowSizeClassInPreview
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.ContentCopy
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScanSource
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.ScannedQRCodeActionListeners
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.StartScanActionListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

// region screen composables
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanQRCodeInfoScreen(
    buttonListeners: StartScanActionListeners = StartScanActionListeners(),
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
    cameraPermissionStatus: PermissionStatus,
    uiState: QRCodeInfoUIState = QRCodeInfoUIState(),
    largeScreen: Boolean = false,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(Dimens.spacingMedium)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
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
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        CameraPermissionContent(cameraPermissionStatus = cameraPermissionStatus)
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        ScanFromCameraButton(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = buttonListeners.onStartScanFromCamera,
            hasScanned = uiState.content is QRCodeInfoContentUIState.CodeScanned,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
        ScanFromImageFileButton(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = buttonListeners.onStartScanFromImageFile,
            hasScanned = uiState.content is QRCodeInfoContentUIState.CodeScanned,
        )
    }
}

@Composable
private fun InitialInfoHeader() {
    Text(
        modifier = Modifier.fillMaxWidth(fraction = 0.8f).padding(vertical = Dimens.spacingMedium),
        text = stringResource(id = R.string.app_name),
        textAlign = TextAlign.Center,
        style =
            MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
            ),
        color = MaterialTheme.colorScheme.primary,
    )
    Text(
        modifier = Modifier.fillMaxWidth(fraction = 0.8f),
        text =
            stringResource(
                id = R.string.scan_code_header,
                stringResource(id = R.string.scan_code_camera_action_button),
            ),
        style = MaterialTheme.typography.titleLarge,
    )
}

@Composable
private fun QRCodeReadContent(
    modifier: Modifier = Modifier,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
    qrCode: ScannedCode,
    largeScreen: Boolean,
) {
    Column(modifier = modifier) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spacingMedium),
            text = stringResource(id = qrCode.source.scannedLabelId),
            textAlign = TextAlign.Center,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold,
                ),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
        )
        HorizontalDivider(
            modifier =
                modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.spacingSmall),
            thickness = Dimens.borderWidthSmall,
            color = MaterialTheme.colorScheme.primaryContainer,
        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spacingMedium),
            text =
                stringResource(
                    id = R.string.scan_code_read_format,
                    stringResource(id = qrCode.format.titleStringId),
                ),
            style = MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
        if (largeScreen) {
            QRCodeContentLargeScreen(qrCode = qrCode.data, actionListeners = actionListeners)
        } else {
            QRCodeContentPortrait(qrCode = qrCode.data, actionListeners = actionListeners)
        }
    }
}

@Composable
private fun QRCodeContentLargeScreen(
    modifier: Modifier = Modifier,
    qrCode: String,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
) {
    Row(modifier = modifier) {
        QRCodeTextContent(
            modifier =
                Modifier
                    .weight(1f)
                    .heightIn(min = 200.dp, max = 500.dp),
            qrCode = qrCode,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.size(Dimens.spacingMedium))
        Card {
            Column(
                modifier =
                    Modifier.padding(
                        vertical = Dimens.spacingMedium,
                        horizontal = Dimens.spacingSmall,
                    ),
            ) {
                QRCodeActionButtons(qrCode = qrCode, actionListeners = actionListeners)
            }
        }
    }
}

@Composable
private fun QRCodeContentPortrait(
    modifier: Modifier = Modifier,
    qrCode: String,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        QRCodeTextContent(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 200.dp),
            qrCode = qrCode,
        )
        Spacer(modifier = Modifier.size(Dimens.spacingSmall))
        Card {
            Row(
                modifier =
                    Modifier.padding(
                        vertical = Dimens.spacingExtraSmall,
                        horizontal = Dimens.spacingMedium,
                    ),
                horizontalArrangement = Arrangement.Center,
            ) {
                QRCodeActionButtons(qrCode = qrCode, actionListeners = actionListeners)
            }
        }
    }
}

@Composable
private fun QRCodeActionButtons(
    qrCode: String,
    actionListeners: ScannedQRCodeActionListeners = ScannedQRCodeActionListeners(),
) {
    IconButton(onClick = { actionListeners.onCodeOpen(qrCode) }) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = stringResource(id = R.string.action_open),
        )
    }
    Spacer(modifier = Modifier.size(Dimens.spacingMedium))
    IconButton(onClick = { actionListeners.onCodeShared(qrCode) }) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.action_share),
        )
    }
    Spacer(modifier = Modifier.size(Dimens.spacingMedium))
    IconButton(onClick = { actionListeners.onCodeCopied(qrCode) }) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
            imageVector = Icons.Outlined.ContentCopy,
            contentDescription = stringResource(id = R.string.action_copy),
        )
    }
}

@ExperimentalPermissionsApi
@Composable
private fun CameraPermissionContent(cameraPermissionStatus: PermissionStatus) {
    when (cameraPermissionStatus) {
        PermissionStatus.Granted -> {
            // nothing to show here, as permission has already been granted
            Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        }

        is PermissionStatus.Denied -> {
            Text(
                modifier = Modifier.fillMaxWidth(fraction = 0.8f),
                text =
                    stringResource(
                        id =
                            if (cameraPermissionStatus.shouldShowRationale) {
                                R.string.scan_code_permission_denied_info
                            } else {
                                R.string.scan_code_permission_info
                            },
                        formatArgs =
                            if (cameraPermissionStatus.shouldShowRationale) {
                                arrayOf(stringResource(id = R.string.scan_code_image_action_button))
                            } else {
                                emptyArray()
                            },
                    ),
            )
            Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))
        }
    }
}
// endregion screen composables

// region screen previews
@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun ScanQRCodeInfoScreenPermissionGrantedPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        ScanQRCodeInfoScreen(
            buttonListeners = StartScanActionListeners(),
            cameraPermissionStatus = PermissionStatus.Granted,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview
@Composable
fun ScanQRCodeInfoScreenPermissionDeniedPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        ScanQRCodeInfoScreen(
            buttonListeners = StartScanActionListeners(),
            cameraPermissionStatus = PermissionStatus.Denied(shouldShowRationale = true),
        )
    }
}

@OptIn(
    ExperimentalPermissionsApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class,
)
@PreviewScreenSizes
@Composable
fun ScanQRCodeInfoScreenWithCodeReadPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        ScanQRCodeInfoScreen(
            buttonListeners = StartScanActionListeners(),
            cameraPermissionStatus = PermissionStatus.Granted,
            uiState =
                QRCodeInfoUIState(
                    QRCodeInfoContentUIState.CodeScanned(
                        qrCode =
                            ScannedCode(
                                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris " +
                                    "nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in " +
                                    "reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla " +
                                    "pariatur. Excepteur sint occaecat cupidatat non proident, " +
                                    "sunt in culpa qui officia deserunt mollit anim id est laborum.",
                                QRCodeComposeXFormat.QR_CODE,
                                ScanSource.CAMERA,
                            ),
                    ),
                ),
            largeScreen = !phoneUI,
        )
    }
}
// endregion screen previews
