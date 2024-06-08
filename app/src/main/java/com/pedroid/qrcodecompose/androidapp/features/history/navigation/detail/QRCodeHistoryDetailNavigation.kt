package com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.copyImageToClipboard
import com.pedroid.qrcodecompose.androidapp.core.presentation.copyTextToClipboard
import com.pedroid.qrcodecompose.androidapp.core.presentation.shareImageToAnotherApp
import com.pedroid.qrcodecompose.androidapp.core.presentation.shareTextToAnotherApp
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.delete.HistoryDeleteConfirmationDialog
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail.HistoryDetailScreen
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail.HistoryDetailUIAction
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail.HistoryDetailUIState
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail.HistoryDetailViewModel
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail.HistoryDetailViewModelFactory
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult
import kotlinx.serialization.Serializable

@Serializable
data class HistoryDetailRoute(val uid: Long)

fun NavGraphBuilder.historyDetailRoute(
    navigationListeners: HistoryDetailNavigationListeners,
    largeScreen: Boolean,
) {
    composable<HistoryDetailRoute> {
        val route = it.toRoute<HistoryDetailRoute>()
        HistoryDetailCoordinator(
            navigationListeners = navigationListeners,
            largeScreen = largeScreen,
            viewModel =
                hiltViewModel<HistoryDetailViewModel, HistoryDetailViewModelFactory>(
                    creationCallback = { factory ->
                        factory.create(entryUid = route.uid)
                    },
                ),
        )
    }
}

@Composable
private fun HistoryDetailCoordinator(
    navigationListeners: HistoryDetailNavigationListeners,
    largeScreen: Boolean,
    viewModel: HistoryDetailViewModel,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDeleteDialog: Boolean by rememberSaveable { mutableStateOf(false) }
    var currentImageBitmap: Bitmap? by remember { mutableStateOf(null) }

    when (uiState) {
        is HistoryDetailUIState.Idle -> {
            // nothing to show, should be transition state
        }
        is HistoryDetailUIState.Deleted -> {
            LaunchedEffect(key1 = uiState) {
                val deletedByUserUid: Long? = (uiState as? HistoryDetailUIState.Deleted)?.deletedItemUid
                if (deletedByUserUid != null) {
                    navigationListeners.onUserDelete(deletedByUserUid)
                } else {
                    navigationListeners.onGoBack()
                }
            }
        }
        is HistoryDetailUIState.Content -> {
            HistoryDetailScreen(
                data = (uiState as HistoryDetailUIState.Content).data,
                largeScreen = largeScreen,
                navigationListeners = navigationListeners,
                actionListeners =
                    HistoryDetailActionListeners(
                        onImageShare = {
                            val result =
                                context.shareImageToAnotherApp(
                                    content = currentImageBitmap,
                                    shareTitle = context.getNameFromFormat(uiState),
                                )
                            viewModel.onNewAction(HistoryDetailUIAction.QRActionComplete(result))
                        },
                        onImageCopyToClipboard = {
                            val result = context.copyImageToClipboard(currentImageBitmap)
                            viewModel.onNewAction(
                                HistoryDetailUIAction.QRActionComplete(result),
                            )
                        },
                        onTextShare = {
                            viewModel.onNewAction(
                                HistoryDetailUIAction.QRActionComplete(
                                    context.shareTextToAnotherApp(it),
                                ),
                            )
                        },
                        onTextCopyToClipboard = {
                            context.copyTextToClipboard(it)
                            viewModel.onNewAction(
                                HistoryDetailUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)),
                            )
                        },
                        generateResult = {
                            if (it is QRCodeGenerateResult.Generated) {
                                currentImageBitmap = it.bitmap
                            }
                            viewModel.onNewAction(HistoryDetailUIAction.Generate(it))
                        },
                        onDeleteItem = {
                            showDeleteDialog = true
                        },
                    ),
            )

            TemporaryMessage(data = (uiState as? HistoryDetailUIState.Content)?.temporaryMessage) {
                viewModel.onNewAction(action = HistoryDetailUIAction.TmpMessageShown)
            }
        }
    }

    if (showDeleteDialog) {
        HistoryDeleteConfirmationDialog(
            title = context.getString(R.string.history_delete_single_item_notice_title),
            description = context.getString(R.string.history_delete_single_item_notice_description),
            deleteActionButton = context.getString(R.string.history_delete_single_item_notice_action),
            onDelete = {
                viewModel.onNewAction(HistoryDetailUIAction.Delete)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            },
        )
    }
}

private fun Context.getNameFromFormat(uiState: HistoryDetailUIState): String =
    this.getString((((uiState as? HistoryDetailUIState.Content)?.data?.format ?: QRCodeComposeXFormat.QR_CODE)).titleStringId)

fun NavController.navigateToHistoryDetail(
    navOptions: NavOptions? = null,
    uid: Long,
) {
    this.navigate(HistoryDetailRoute(uid), navOptions)
}
