package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
fun PermissionState.launchPermissionRequestOrRun(onPermissionGranted: () -> Unit) {
    if (status.isGranted) {
        onPermissionGranted()
    } else {
        launchPermissionRequest()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberPermissionState(
    permission: String,
    onPermissionGranted: () -> Unit,
) = rememberPermissionState(
    permission = permission,
    onPermissionResult = { granted ->
        if (granted) {
            onPermissionGranted()
        }
    },
)
