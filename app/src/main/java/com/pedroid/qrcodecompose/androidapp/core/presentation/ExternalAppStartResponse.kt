package com.pedroid.qrcodecompose.androidapp.core.presentation

sealed class ExternalAppStartResponse {
    abstract val status: AppResponseStatus

    data class OpenApp(override val status: AppResponseStatus) : ExternalAppStartResponse()

    data class ShareApp(override val status: AppResponseStatus) : ExternalAppStartResponse()
}

enum class AppResponseStatus {
    SUCCESS,
    ERROR_NO_APP,
}

fun ExternalAppStartResponse.getErrorMessageKey(): String? {
    return if (this.status == AppResponseStatus.ERROR_NO_APP) {
        when (this) {
            is ExternalAppStartResponse.OpenApp -> {
                "code_open_app_error"
            }

            is ExternalAppStartResponse.ShareApp -> {
                "code_share_app_error"
            }
        }
    } else {
        null
    }
}
