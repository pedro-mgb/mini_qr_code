package com.pedroid.qrcodecompose.androidapp.core.presentation

sealed class QRAppActions {
    abstract val status: ActionStatus

    data class OpenApp(override val status: ActionStatus) : QRAppActions()

    data class ShareApp(override val status: ActionStatus) : QRAppActions()

    data class Copy(override val status: ActionStatus = ActionStatus.SUCCESS) : QRAppActions()

    data class SaveToFile(override val status: ActionStatus) : QRAppActions()
}

enum class ActionStatus {
    SUCCESS,
    ERROR_FILE,
    ERROR_NO_APP,
}

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
