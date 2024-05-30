package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListScreen
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListUIAction
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListUIState
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryListViewModel
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryMoreInfoBottomSheet
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.delete.HistoryDeleteConfirmationDialog

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

    var deleteItemCountShowDialog: Int? by rememberSaveable { mutableStateOf(null) }

    (savedStateHandle.get<Long?>(DELETED_HISTORY_ITEM_UID_KEY))?.let {
        LaunchedEffect(key1 = it) {
            viewModel.onNewAction(HistoryListUIAction.DeletedFromDetails(it))
        }
    }

    HistoryListScreen(
        content = uiState.content,
        selectionMode = uiState.selectionMode,
        navigationListeners = navigationListeners,
        actionListeners =
            HistoryListActionListeners(
                onMoreInfoRequested = {
                    showMoreInfoBottomSheet = true
                },
                onItemSelectedToggle = {
                    viewModel.onNewAction(HistoryListUIAction.SelectedItemToggle(it))
                },
                onSelectAllItems = {
                    viewModel.onNewAction(HistoryListUIAction.SelectAll)
                },
                onSelectionBackPressed = {
                    viewModel.onNewAction(HistoryListUIAction.CancelSelection)
                },
                onDeletePressed = { deleteCount ->
                    deleteItemCountShowDialog = deleteCount
                },
            ),
    )

    if (showMoreInfoBottomSheet) {
        HistoryMoreInfoBottomSheet(onDismiss = { showMoreInfoBottomSheet = false })
    } else if (deleteItemCountShowDialog.let { it != null && it > 0 }) {
        HistoryListDeleteItemsDialog(
            deleteCount = deleteItemCountShowDialog ?: 1,
            deleteAction = {
                viewModel.onNewAction(HistoryListUIAction.DeleteSelected)
                deleteItemCountShowDialog = null
            },
            dismissAction = {
                deleteItemCountShowDialog = null
            },
        )
    }
    TemporaryMessage(data = uiState.temporaryMessage) {
        viewModel.onNewAction(action = HistoryListUIAction.TmpMessageShown)
    }
}

@Composable
fun HistoryListDeleteItemsDialog(
    deleteCount: Int,
    deleteAction: () -> Unit,
    dismissAction: () -> Unit,
) {
    HistoryDeleteConfirmationDialog(
        title =
            if (deleteCount <= 1) {
                stringResource(id = R.string.history_delete_single_item_notice_title)
            } else {
                stringResource(id = R.string.history_delete_multiple_items_notice_title, deleteCount)
            },
        description =
            if (deleteCount <= 1) {
                stringResource(id = R.string.history_delete_single_item_notice_description)
            } else {
                stringResource(id = R.string.history_delete_multiple_items_notice_description, deleteCount)
            },
        deleteActionButton =
            if (deleteCount <= 1) {
                stringResource(id = R.string.history_delete_single_item_notice_action)
            } else {
                stringResource(id = R.string.history_delete_multiple_items_notice_action, deleteCount)
            },
        onDelete = deleteAction,
        onDismiss = dismissAction,
    )
}

fun NavController.navigateToQRCodeHistoryList(navOptions: NavOptions? = null) {
    this.navigate(HISTORY_LIST_ROUTE, navOptions)
}
