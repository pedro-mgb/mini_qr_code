package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListScreen
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListUIAction
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListUIState
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListViewModel
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryMoreInfoBottomSheet

const val HISTORY_LIST_ROUTE = "QR_CODE_HISTORY_LIST_ROUTE"

fun NavGraphBuilder.historyListRoute(navigationListeners: HistoryListNavigationListeners) {
    composable(route = HISTORY_LIST_ROUTE) {
        HistoryListCoordinator(navigationListeners, it.savedStateHandle)
    }
}

@Composable
private fun HistoryListCoordinator(
    navigationListeners: HistoryListNavigationListeners,
    savedStateHandle: SavedStateHandle,
    viewModel: HistoryListViewModel = hiltViewModel(),
) {
    val uiState: HistoryListUIState by viewModel.uiState.collectAsStateWithLifecycle()

    var showMoreInfoBottomSheet: Boolean by rememberSaveable { mutableStateOf(false) }

    (savedStateHandle.get<Long?>(DELETED_HISTORY_ITEM_UID_KEY))?.let {
        LaunchedEffect(key1 = it) {
            viewModel.onNewAction(HistoryListUIAction.DeletedFromDetails(it))
        }
    }

    HistoryListScreen(
        content = uiState.content,
        navigationListeners = navigationListeners,
        actionListeners =
            HistoryListActionListeners(
                onMoreInfoRequested = {
                    showMoreInfoBottomSheet = true
                },
            ),
    )

    if (showMoreInfoBottomSheet) {
        HistoryMoreInfoBottomSheet(onDismiss = { showMoreInfoBottomSheet = false })
    }
    TemporaryMessage(data = uiState.temporaryMessage) {
        viewModel.onNewAction(action = HistoryListUIAction.TmpMessageShown)
    }
}

fun NavController.navigateToQRCodeHistoryList(navOptions: NavOptions? = null) {
    this.navigate(HISTORY_LIST_ROUTE, navOptions)
}
