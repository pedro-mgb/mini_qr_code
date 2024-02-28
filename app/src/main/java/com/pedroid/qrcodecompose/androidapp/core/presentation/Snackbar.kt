package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun Snackbar(
    messageKey: String,
    onSnackbarShown: () -> Unit = {},
) {
    val context = LocalContext.current
    val snackbarHostState = LocalSnackbarHostState.current
    LaunchedEffect(key1 = messageKey) {
        if (messageKey.isNotBlank()) {
            val message = context.getString(messageKey) ?: ""
            if (message.isNotBlank()) {
                snackbarHostState?.showSnackbar(message)
                onSnackbarShown()
            }
        }
    }
}
