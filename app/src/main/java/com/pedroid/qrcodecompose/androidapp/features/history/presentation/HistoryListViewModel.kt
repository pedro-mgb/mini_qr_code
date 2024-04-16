package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.core.presentation.isToday
import com.pedroid.qrcodecompose.androidapp.core.presentation.isYesterday
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
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
        historyRepository: HistoryRepository,
        private val logger: Logger,
    ) : ViewModel() {
        val uiState: StateFlow<HistoryListUIState> =
            this.createStateFlow(
                originalFlow = historyRepository.getAllHistory(),
                initialValue = HistoryListUIState(HistoryListContentState.Idle),
                mapper = ::mapToUIState,
            )

        private val defaultZoneId get() = ZoneId.systemDefault()
        private val defaultLocale get() = Locale.getDefault()
        private val timeStampPattern get() = "dd-MM-yyyy\n${getHourTimeFormat()}"

        private fun mapToUIState(entries: List<HistoryEntry>): HistoryListUIState {
            logger.debug(LOG_TAG, "received list from repo: $entries")

            val contentState: HistoryListContentState =
                if (entries.isEmpty()) {
                    HistoryListContentState.Empty
                } else {
                    val uiStateList = mutableListOf<HistoryListItem>()
                    uiStateList.appendHistoryData(entries)
                    HistoryListContentState.DataList(uiStateList.toList())
                }

            logger.debug(LOG_TAG, "converted history to ui state content: $contentState")
            return HistoryListUIState(contentState)
        }

        private fun MutableList<HistoryListItem>.appendHistoryData(entries: List<HistoryEntry>) {
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
                    this.add(historyEntry.toHistoryListItemData())
                }
            }
        }

        private fun HistoryEntry.toHistoryListItemData() =
            HistoryListItem.Data(
                uid = this.uid,
                value = this.value,
                displayDate =
                    this.creationDate
                        .atZone(defaultZoneId)
                        .format(DateTimeFormatter.ofPattern(timeStampPattern, defaultLocale)),
                typeUI = this.getTypeUI(),
                formatStringId = this.format.titleStringId,
            )

        private fun HistoryEntry.getTypeUI(): HistoryTypeUI =
            when (this) {
                is HistoryEntry.Scan -> {
                    if (!this.fromImageFile) {
                        HistoryTypeUI.SCAN_CAMERA
                    } else {
                        HistoryTypeUI.SCAN_FILE
                    }
                }
                is HistoryEntry.Generate -> HistoryTypeUI.GENERATE
            }
    }

@Parcelize
data class HistoryListUIState(val content: HistoryListContentState) : Parcelable

sealed class HistoryListContentState : Parcelable {
    @Parcelize
    data object Idle : HistoryListContentState()

    @Parcelize
    data object Empty : HistoryListContentState()

    @Parcelize
    data class DataList(val list: List<HistoryListItem>) : HistoryListContentState()
}
