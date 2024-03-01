package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

data class GeneratedQRCodeUpdateListeners(
    val onTextUpdated: (String) -> Unit = {},
    val onGeneratorResult: (QRCodeGenerateResult) -> Unit = {},
)

data class GenerateQRCodeActionListeners(
    val onImageSaveToFile: () -> Unit = {},
    val onImageShare: () -> Unit = {},
    val onImageCopyToClipboard: () -> Unit = {},
)
