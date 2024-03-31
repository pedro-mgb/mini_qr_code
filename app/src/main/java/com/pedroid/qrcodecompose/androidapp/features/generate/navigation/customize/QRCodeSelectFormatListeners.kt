package com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

data class QRCodeSelectFormatListeners(
    val onSelectFormat: (QRCodeComposeXFormat) -> Unit = {},
    val onCancel: () -> Unit = {},
)
