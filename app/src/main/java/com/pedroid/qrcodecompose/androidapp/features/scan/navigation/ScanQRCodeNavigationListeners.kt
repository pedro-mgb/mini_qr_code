package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

data class ScanQRCodeInfoNavigationListeners(
    val onGoScanQRCode: () -> Unit,
)

data class ScanQRCodeCameraNavigationListeners(
    val onCodeScanned: (String) -> Unit,
    val onBackInvoked: () -> Unit,
)

data class StartScanActionListeners(
    val onStartScanFromCamera: () -> Unit = {},
    val onStartScanFromImageFile: () -> Unit = {},
)

data class ScannedQRCodeActionListeners(
    val onCodeOpen: (String) -> Unit = {},
    val onCodeShared: (String) -> Unit = {},
    val onCodeCopied: (String) -> Unit = {},
)
