package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.core.presentation.getHourTimeFormat
import com.pedroid.qrcodecompose.androidapp.core.presentation.isToday
import com.pedroid.qrcodecompose.androidapp.core.presentation.isYesterday
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private const val LOG_TAG = "HistoryListViewModel"

// Friday, 12 April 2024
private const val DATE_HEADER_PATTERN = "EEEE, d MMM yyyy"

@HiltViewModel
class HistoryListViewModel
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
        private val logger: Logger,
    ) : ViewModel() {
        private val temporaryMessageFlow = MutableStateFlow<TemporaryMessageData?>(null)
        private val itemUidsSelectedFlow = MutableStateFlow<Set<Long>>(emptySet())
        private val originalFlow =
            combine(
                historyRepository.getAllHistory(),
                itemUidsSelectedFlow,
                temporaryMessageFlow,
            ) { historyEntries, selectedItems, tmpMessage ->
                CombinedHistoryStates(historyEntries, selectedItems, tmpMessage)
            }

        val uiState: StateFlow<HistoryListUIState> =
            this.createStateFlow(
                originalFlow = originalFlow,
                initialValue = HistoryListUIState(HistoryListContentState.Idle),
                mapper = ::mapToUIState,
            )

        private val defaultZoneId get() = ZoneId.systemDefault()
        private val defaultLocale get() = Locale.getDefault()
        private val timeStampPattern get() = "dd-MM-yyyy\n${getHourTimeFormat()}"

        private val deletedItemsUids: MutableList<Long> = mutableListOf()

        private fun mapToUIState(combinedStates: CombinedHistoryStates): HistoryListUIState {
            logger.debug(LOG_TAG, "received list from repo + additional states: $combinedStates")

            var selectedItems = 0
            val contentState: HistoryListContentState =
                if (combinedStates.historyEntries.isEmpty()) {
                    HistoryListContentState.Empty
                } else {
                    val uiStateList = mutableListOf<HistoryListItem>()
                    selectedItems =
                        uiStateList.appendHistoryData(
                            combinedStates.historyEntries,
                            combinedStates.selectedItemUids.toList(),
                        )
                    HistoryListContentState.DataList(uiStateList.toList())
                }

            logger.debug(LOG_TAG, "converted history to ui state content: $contentState")
            return HistoryListUIState(
                content = contentState,
                selectionMode =
                    if (selectedItems > 0) {
                        HistorySelectionMode.Selection(selectedItems)
                    } else {
                        HistorySelectionMode.None
                    },
                temporaryMessage = combinedStates.temporaryMessage,
            )
        }

        private fun MutableList<HistoryListItem>.appendHistoryData(
            entries: List<HistoryEntry>,
            selectedItemUids: List<Long>,
        ): Int {
            var selectedItemsCount = 0
            // entries should already be sorted from data source, but this guarantees it
            val sortedEntries =
                entries.sortedByDescending {
                    it.creationDate
                }
            val groupedEntriesByDay =
                sortedEntries.groupBy {
                    it.creationDate.atZone(defaultZoneId).toLocalDate()
                }
            groupedEntriesByDay.forEach { (localDate, historyEntries) ->
                val headerText: String =
                    when {
                        localDate.isToday() -> "history_header_today"
                        localDate.isYesterday() -> "history_header_yesterday"
                        else -> localDate.format(DateTimeFormatter.ofPattern(DATE_HEADER_PATTERN, defaultLocale))
                    }
                this.add(HistoryListItem.SectionHeader(headerText))

                historyEntries.forEach { historyEntry ->
                    val isSelected = historyEntry.uid in selectedItemUids
                    this.add(historyEntry.toHistoryListItemData(isSelected))
                    if (isSelected) {
                        selectedItemsCount++
                    }
                }
            }
            return selectedItemsCount
        }

        private fun HistoryEntry.toHistoryListItemData(isSelected: Boolean) =
            HistoryListItem.Data(
                uid = this.uid,
                value = this.value,
                displayDate =
                    this.creationDate
                        .atZone(defaultZoneId)
                        .format(DateTimeFormatter.ofPattern(timeStampPattern, defaultLocale)),
                typeUI = this.getTypeUI(),
                formatStringId = this.format.titleStringId,
                selected = isSelected,
            )

        fun onNewAction(action: HistoryListUIAction) {
            viewModelScope.launch {
                logger.debug(LOG_TAG, "onNewAction - $action")
                when (action) {
                    is HistoryListUIAction.DeletedFromDetails -> {
                        if (action.deletedUid in deletedItemsUids) {
                            // item deletion has already been notified
                        } else {
                            deletedItemsUids.add(action.deletedUid)
                            emitDeleteSuccessMessage()
                        }
                    }
                    is HistoryListUIAction.SelectedItemToggle -> {
                        itemUidsSelectedFlow.update { set ->
                            if (action.selectedUid in set) {
                                set.toMutableSet().apply {
                                    remove(action.selectedUid)
                                }.toSet()
                            } else {
                                set.toMutableSet().apply {
                                    add(action.selectedUid)
                                }.toSet()
                            }
                        }
                    }
                    HistoryListUIAction.SelectAll -> {
                        val selectedItems =
                            (uiState.value.content as? HistoryListContentState.DataList)?.list
                                ?.filterIsInstance<HistoryListItem.Data>()
                                ?.map {
                                    it.uid
                                } ?: emptyList()
                        itemUidsSelectedFlow.emit(selectedItems.toSet())
                    }
                    HistoryListUIAction.CancelSelection -> {
                        itemUidsSelectedFlow.emit(emptySet())
                    }
                    HistoryListUIAction.DeleteSelected -> {
                        val listToDelete = itemUidsSelectedFlow.value.toList()
                        deletedItemsUids.addAll(listToDelete)
                        historyRepository.deleteHistoryEntries(listToDelete)
                        itemUidsSelectedFlow.emit(emptySet())
                        // Minor implementation detail: There's no error message displaying here, as
                        //  history is in a local database; we could add some prevention code
                        //  but in case an exception is thrown in historyRepository.deleteHistoryEntries(listToDelete)
                        //  then the coroutine fails, and nothing happens; so we can assume if it reaches this block that it succeeds
                        emitDeleteSuccessMessage()
                    }
                    HistoryListUIAction.TmpMessageShown -> {
                        temporaryMessageFlow.emit(null)
                    }
                }
            }
        }

        private suspend fun emitDeleteSuccessMessage() {
            temporaryMessageFlow.emit(
                TemporaryMessageData(
                    text = "history_delete_items_success_message",
                    type = TemporaryMessageType.INFO_SNACKBAR,
                ),
            )
        }

        private data class CombinedHistoryStates(
            val historyEntries: List<HistoryEntry> = emptyList(),
            val selectedItemUids: Set<Long> = emptySet(),
            val temporaryMessage: TemporaryMessageData? = null,
        )
    }

@Parcelize
data class HistoryListUIState(
    val content: HistoryListContentState,
    val selectionMode: HistorySelectionMode = HistorySelectionMode.None,
    val temporaryMessage: TemporaryMessageData? = null,
) : Parcelable {
    @IgnoredOnParcel
    val allSelected: Boolean =
        selectionMode is HistorySelectionMode.Selection &&
            (content as? HistoryListContentState.DataList)?.list
                ?.filterIsInstance<HistoryListItem.Data>()?.all { it.selected } == true
}

sealed class HistoryListContentState : Parcelable {
    @Parcelize
    data object Idle : HistoryListContentState()

    @Parcelize
    data object Empty : HistoryListContentState()

    @Parcelize
    data class DataList(val list: List<HistoryListItem>) : HistoryListContentState()
}

sealed class HistorySelectionMode : Parcelable {
    @Parcelize
    data object None : HistorySelectionMode()

    @Parcelize
    data class Selection(val currentlySelected: Int) : HistorySelectionMode()
}

sealed class HistoryListUIAction {
    data class DeletedFromDetails(val deletedUid: Long) : HistoryListUIAction()

    data class SelectedItemToggle(val selectedUid: Long) : HistoryListUIAction()

    data object SelectAll : HistoryListUIAction()

    data object CancelSelection : HistoryListUIAction()

    data object DeleteSelected : HistoryListUIAction()

    data object TmpMessageShown : HistoryListUIAction()
}
