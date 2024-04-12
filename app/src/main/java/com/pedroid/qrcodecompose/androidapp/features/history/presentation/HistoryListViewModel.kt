package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.core.presentation.isToday
import com.pedroid.qrcodecompose.androidapp.core.presentation.isYesterday
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private const val LOG_TAG = "HistoryListViewModel"

// Friday, 12 April 2024
private const val DATE_HEADER_PATTERN = "EEEE, d MMM yyyy"

// 12-04-2024\n18:00:00
private const val TIMESTAMP_PATTERN = "dd-MM-yyyy\nhh:mm:ss"

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
                initialValue = HistoryListUIState(listOf(HistoryListItem.InfoHeader)),
                mapper = ::mapToUIState,
            )

        private val defaultZoneId get() = ZoneId.systemDefault()
        private val defaultLocale get() = Locale.getDefault()

        private fun mapToUIState(entries: List<HistoryEntry>): HistoryListUIState {
            logger.debug(LOG_TAG, "received list from repo: $entries")
            val uiStateList = mutableListOf<HistoryListItem>(HistoryListItem.InfoHeader)

            if (entries.isEmpty()) {
                uiStateList.add(HistoryListItem.EmptyList)
            } else {
                uiStateList.appendHistoryData(entries)
            }

            logger.debug(LOG_TAG, "converted history ui state list: ${uiStateList.toList()}")
            return HistoryListUIState(uiStateList.toList())
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
                // TODO fix date conversion
                formattedDate =
                    this.creationDate
                        .atZone(defaultZoneId)
                        .format(DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN, defaultLocale)),
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

data class HistoryListUIState(val list: List<HistoryListItem>)
