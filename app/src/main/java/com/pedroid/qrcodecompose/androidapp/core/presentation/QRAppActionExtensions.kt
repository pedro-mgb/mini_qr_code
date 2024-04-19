package com.pedroid.qrcodecompose.androidapp.core.presentation

import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageType

fun QRAppActions.asTemporaryMessage(): TemporaryMessageData? {
    return when (this.status) {
        ActionStatus.ERROR_NO_APP -> {
            this.asErrorNoApp()
        }

        ActionStatus.ERROR_FILE -> {
            TemporaryMessageData(
                text = "code_saved_to_file_error",
                type = TemporaryMessageType.ERROR_SNACKBAR,
            )
        }

        ActionStatus.SUCCESS -> {
            this.asSuccess()
        }
    }
}

private fun QRAppActions.asErrorNoApp() =
    when (this) {
        is QRAppActions.OpenApp -> {
            TemporaryMessageData(
                text = "code_open_app_error",
                type = TemporaryMessageType.ERROR_SNACKBAR,
            )
        }

        is QRAppActions.ShareApp -> {
            TemporaryMessageData(
                text = "code_share_app_error",
                type = TemporaryMessageType.ERROR_SNACKBAR,
            )
        }

        else -> {
            null
        }
    }

private fun QRAppActions.asSuccess() =
    when (this) {
        is QRAppActions.Copy -> {
            TemporaryMessageData(
                text = "code_copied_success",
                type = TemporaryMessageType.SUCCESS_SNACKBAR,
            )
        }

        is QRAppActions.SaveToFile -> {
            TemporaryMessageData(
                text = "code_saved_to_file_success",
                type = TemporaryMessageType.SUCCESS_SNACKBAR,
            )
        }

        else -> {
            null
        }
    }
