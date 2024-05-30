package com.pedroid.qrcodecompose.androidapp.features.history.navigation

data class HistoryListNavigationListeners(
    val onSelectItem: (uid: Long) -> Unit = {},
)

data class HistoryListActionListeners(
    val onMoreInfoRequested: () -> Unit = {},
    val onItemSelectedToggle: (Long) -> Unit = {},
    val onSelectAllItems: () -> Unit = {},
    val onSelectionBackPressed: () -> Unit = {},
    val onDeletePressed: (Int) -> Unit = {},
)
