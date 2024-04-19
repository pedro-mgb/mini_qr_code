package com.pedroid.qrcodecompose.androidapp.core.domain

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
