package com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile

data class ScanFromFileActionListeners(
    val onRetryScan: () -> Unit = {},
    val onCancel: () -> Unit = {},
)
