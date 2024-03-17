package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.pedroid.qrcodecompose.androidapp.R

// region composables
@Composable
fun ScanFromCameraButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    hasScanned: Boolean,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(
            text =
                if (hasScanned) {
                    stringResource(id = R.string.scan_another_code_camera_action_button)
                } else {
                    stringResource(id = R.string.scan_code_camera_action_button)
                },
        )
    }
}

@Composable
fun ScanFromImageFileButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    hasScanned: Boolean,
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(
            text =
                if (hasScanned) {
                    stringResource(id = R.string.scan_another_code_image_action_button)
                } else {
                    stringResource(id = R.string.scan_code_image_action_button)
                },
        )
    }
}
// endregion composables
