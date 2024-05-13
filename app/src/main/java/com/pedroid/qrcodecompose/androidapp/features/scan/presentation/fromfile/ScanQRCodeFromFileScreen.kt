package com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.getWindowSizeClassInPreview
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppToolbar
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.ScanFromFileActionListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

// region composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanQRCodeFromFileScreen(
    uiState: QRCodeFromFileUIState,
    scanFromFileActionListeners: ScanFromFileActionListeners,
    largeScreen: Boolean = false,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QRAppToolbar(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.scan_code_from_file_toolbar_title,
            onNavigationIconClick = scanFromFileActionListeners.onCancel,
        )
        if (uiState is QRCodeFromFileUIState.Error) {
            FileScanErrorScreen(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                scanFromFileActionListeners = scanFromFileActionListeners,
                largeScreen = largeScreen,
            )
        } else {
            LoadingFromFile(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
            )
        }
    }
}

@Composable
private fun FileScanErrorScreen(
    modifier: Modifier = Modifier,
    scanFromFileActionListeners: ScanFromFileActionListeners,
    largeScreen: Boolean,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (largeScreen) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ErrorScreenHeaderContent()
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                ErrorScreenHeaderContent()
            }
        }

        Spacer(modifier = Modifier.size(Dimens.spacingExtraSmall))
        Text(
            modifier = Modifier.padding(Dimens.spacingLarge),
            text = stringResource(id = R.string.scan_code_from_file_error_description),
        )
        Spacer(modifier = Modifier.size(Dimens.spacingMedium))

        Button(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = scanFromFileActionListeners.onRetryScan,
        ) {
            Text(text = stringResource(id = R.string.action_try_again))
        }
        Spacer(modifier = Modifier.height(Dimens.spacingSmall))
        OutlinedButton(
            modifier = Modifier.fillMaxWidth(fraction = 0.6f),
            onClick = scanFromFileActionListeners.onCancel,
        ) {
            Text(text = stringResource(id = R.string.action_cancel))
        }
    }
}

@Composable
private fun ErrorScreenHeaderContent() {
    Box {
        Icon(
            modifier = Modifier.size(80.dp),
            imageVector = Icons.Filled.ScanQRCode,
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f),
            contentDescription = null,
        )
        Icon(
            modifier =
                Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd),
            imageVector = Icons.Filled.Close,
            tint = MaterialTheme.colorScheme.onError,
            contentDescription = null,
        )
    }
    Spacer(modifier = Modifier.size(Dimens.spacingSmall))
    Text(
        text = stringResource(id = R.string.scan_code_from_file_error_title),
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.error,
    )
}

@Composable
private fun LoadingFromFile(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(Dimens.loadingIndicatorSizeLarge))
    }
}
// endregion composables

// region previews
@Preview
@Composable
private fun ScanQRCodeFromFileSuccessPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        ScanQRCodeFromFileScreen(
            uiState =
                QRCodeFromFileUIState.Success(
                    qrCode = "some qr code",
                    format = QRCodeComposeXFormat.QR_CODE,
                ),
            scanFromFileActionListeners = ScanFromFileActionListeners(),
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@PreviewScreenSizes
@Composable
private fun ScanQRCodeFromFileErrorPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        ScanQRCodeFromFileScreen(
            uiState = QRCodeFromFileUIState.Error,
            scanFromFileActionListeners = ScanFromFileActionListeners(),
            largeScreen = !phoneUI,
        )
    }
}
// endregion previews
