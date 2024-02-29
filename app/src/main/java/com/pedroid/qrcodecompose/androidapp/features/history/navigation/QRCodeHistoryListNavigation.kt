package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListScreen

const val HISTORY_LIST_ROUTE = "QR_CODE_HISTORY_LIST_ROUTE"

fun NavGraphBuilder.historyListRoute() {
    composable(route = HISTORY_LIST_ROUTE) {
        HistoryListScreen()
    }
}

fun NavController.navigateToQRCodeHistoryList(navOptions: NavOptions? = null) {
    this.navigate(HISTORY_LIST_ROUTE, navOptions)
}
