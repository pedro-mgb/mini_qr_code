package com.pedroid.qrcodecompose.androidapp.features.scan.navigation

data class ScanQRCodeInfoNavigationListeners(
    val onGoScanQRCode: () -> Unit
)

data class ScanQRCodeCameraNavigationListeners(
    val onCodeScanned: (String) -> Unit
)