package com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail

import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

data class HistoryDetailNavigationListeners(
    val onUserDelete: (Long) -> Unit = {},
    val onGoBack: () -> Unit = {},
    val onExpand: (ExpandQRCodeArguments) -> Unit = {},
)

data class HistoryDetailActionListeners(
    val onImageShare: () -> Unit = {},
    val onImageCopyToClipboard: () -> Unit = {},
    val onTextShare: (String) -> Unit = {},
    val onTextCopyToClipboard: (String) -> Unit = {},
    val generateResult: (QRCodeGenerateResult) -> Unit = {},
    val onDeleteItem: () -> Unit = {},
)
