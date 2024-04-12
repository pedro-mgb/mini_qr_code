package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListScreen
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListUIState
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListViewModel

const val HISTORY_LIST_ROUTE = "QR_CODE_HISTORY_LIST_ROUTE"

fun NavGraphBuilder.historyListRoute() {
    composable(route = HISTORY_LIST_ROUTE) {
        HistoryListCoordinator()
    }
}

@Composable
private fun HistoryListCoordinator(viewModel: HistoryListViewModel = hiltViewModel()) {
    val uiState: HistoryListUIState by viewModel.uiState.collectAsStateWithLifecycle()

    HistoryListScreen(content = uiState.list)
}

fun NavController.navigateToQRCodeHistoryList(navOptions: NavOptions? = null) {
    this.navigate(HISTORY_LIST_ROUTE, navOptions)
}
