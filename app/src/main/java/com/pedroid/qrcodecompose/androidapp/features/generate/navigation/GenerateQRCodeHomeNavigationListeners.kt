package com.pedroid.qrcodecompose.androidapp.features.generate.navigation

import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeCustomizationOptions
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

data class GenerateQRCodeHomeNavigationListeners(
    val onCustomize: (currentOptions: QRCodeCustomizationOptions) -> Unit,
)

data class GeneratedQRCodeUpdateListeners(
    val onTextUpdated: (String) -> Unit = {},
    val onGeneratorResult: (QRCodeGenerateResult) -> Unit = {},
)

data class GenerateQRCodeActionListeners(
    val onCustomize: () -> Unit = {},
    val onImageSaveToFile: () -> Unit = {},
    val onImageShare: () -> Unit = {},
    val onImageCopyToClipboard: () -> Unit = {},
)
