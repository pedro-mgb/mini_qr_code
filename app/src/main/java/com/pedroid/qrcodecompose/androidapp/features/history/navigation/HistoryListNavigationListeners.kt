package com.pedroid.qrcodecompose.androidapp.features.history.navigation

data class HistoryListNavigationListeners(
    val onSelectItem: (uid: Long) -> Unit = {},
)
