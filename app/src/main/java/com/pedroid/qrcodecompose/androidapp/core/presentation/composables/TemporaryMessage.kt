package com.pedroid.qrcodecompose.androidapp.core.presentation.composables

import android.content.Context
import android.os.Parcelable
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.pedroid.qrcodecompose.androidapp.core.presentation.LocalSnackbarHostState
import com.pedroid.qrcodecompose.androidapp.core.presentation.getString
import com.pedroid.qrcodecompose.androidapp.core.presentation.showToast
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppSnackbarVisuals
import com.pedroid.qrcodecompose.androidapp.designsystem.components.SnackbarType
import kotlinx.parcelize.Parcelize

// region composables
@Composable
fun TemporaryMessage(
    data: TemporaryMessageData?,
    context: Context = LocalContext.current,
    snackbarHostState: SnackbarHostState? = LocalSnackbarHostState.current,
    onMessageShown: () -> Unit = {},
) {
    LaunchedEffect(key1 = data) {
        if (data == null || data.text.isBlank()) {
            return@LaunchedEffect
        }
        if (data.type == TemporaryMessageType.TOAST) {
            context.showToast(data.text)
        } else {
            snackbarHostState?.showSnackbar(data.toSnackbarVisuals(context))
        }
        onMessageShown()
    }
}
// endregion composables

// region data
@Parcelize
data class TemporaryMessageData(
    val text: String,
    val type: TemporaryMessageType,
) : Parcelable {
    companion object {
        fun error(text: String): TemporaryMessageData =
            TemporaryMessageData(
                text = text,
                type = TemporaryMessageType.ERROR_SNACKBAR,
            )
    }
}

enum class TemporaryMessageType {
    TOAST,
    INFO_SNACKBAR,
    SUCCESS_SNACKBAR,
    ERROR_SNACKBAR,
}

private fun TemporaryMessageData.toSnackbarVisuals(context: Context): QRAppSnackbarVisuals =
    QRAppSnackbarVisuals(
        type = this.type.toSnackbarType(),
        message = context.getString(this.text),
    )

private fun TemporaryMessageType.toSnackbarType(): SnackbarType {
    return when (this) {
        TemporaryMessageType.TOAST -> SnackbarType.INFO
        TemporaryMessageType.INFO_SNACKBAR -> SnackbarType.INFO
        TemporaryMessageType.SUCCESS_SNACKBAR -> SnackbarType.SUCCESS
        TemporaryMessageType.ERROR_SNACKBAR -> SnackbarType.ERROR
    }
}
// endregion data
