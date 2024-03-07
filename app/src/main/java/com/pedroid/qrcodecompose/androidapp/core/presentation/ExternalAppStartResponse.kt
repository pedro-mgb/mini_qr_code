package com.pedroid.qrcodecompose.androidapp.core.presentation

sealed class ExternalAppStartResponse {
    abstract val status: AppResponseStatus

    data class OpenApp(override val status: AppResponseStatus) : ExternalAppStartResponse()

    data class ShareApp(override val status: AppResponseStatus) : ExternalAppStartResponse()
}

enum class AppResponseStatus {
    SUCCESS,
    ERROR_FILE,
    ERROR_NO_APP,
}

fun ExternalAppStartResponse.asTemporaryMessage(): TemporaryMessageData? {
    return when (this.status) {
        AppResponseStatus.ERROR_NO_APP -> {
            when (this) {
                is ExternalAppStartResponse.OpenApp -> {
                    TemporaryMessageData(
                        text = "code_open_app_error",
                        type = TemporaryMessageType.ERROR_SNACKBAR,
                    )
                }

                is ExternalAppStartResponse.ShareApp -> {
                    TemporaryMessageData(
                        text = "code_share_app_error",
                        type = TemporaryMessageType.ERROR_SNACKBAR,
                    )
                }
            }
        }
        AppResponseStatus.ERROR_FILE -> {
            TemporaryMessageData(
                text = "code_saved_to_file_error",
                type = TemporaryMessageType.ERROR_SNACKBAR,
            )
        }
        else -> {
            null
        }
    }
}
