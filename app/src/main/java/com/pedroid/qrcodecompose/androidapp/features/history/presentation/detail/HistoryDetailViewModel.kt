package com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.core.presentation.getHourTimeFormat
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.getTypeUI
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val LOG_TAG = "HistoryDetailViewModel"

@HiltViewModel(assistedFactory = HistoryDetailViewModelFactory::class)
class HistoryDetailViewModel
    @AssistedInject
    constructor(
        @Assisted private val entryUid: Long,
        historyRepository: HistoryRepository,
        private val logger: Logger,
    ) : ViewModel() {
        val uiState: StateFlow<HistoryDetailUIState> =
            this.createStateFlow(
                originalFlow = historyRepository.getSingleHistory(entryUid),
                initialValue = HistoryDetailUIState.Idle,
                mapper = ::mapToUIState,
            )

        private val defaultZoneId get() = ZoneId.systemDefault()
        private val defaultLocale get() = Locale.getDefault()
        private val dateTimePattern get() = "EEEE, d MMM yyyy - ${getHourTimeFormat()}"

        private fun mapToUIState(entry: HistoryEntry?): HistoryDetailUIState {
            logger.debug(LOG_TAG, "history entry received - $entry")
            val state: HistoryDetailUIState =
                if (entry == null) {
                    HistoryDetailUIState.Deleted
                } else {
                    HistoryDetailUIState.Content(data = entry.toHistoryDetail())
                }
            logger.debug(LOG_TAG, "history entry received - $entry")
            return state
        }

        private fun HistoryEntry.toHistoryDetail(): HistoryDetail =
            HistoryDetail(
                uid = this.uid,
                value = value,
                format = this.format,
                displayDate =
                    this.creationDate
                        .atZone(defaultZoneId)
                        .format(DateTimeFormatter.ofPattern(dateTimePattern, defaultLocale)),
                typeUI = this.getTypeUI(),
            )
    }

@AssistedFactory
interface HistoryDetailViewModelFactory {
    fun create(entryUid: Long): HistoryDetailViewModel
}

sealed class HistoryDetailUIState {
    data object Idle : HistoryDetailUIState()

    data object Deleted : HistoryDetailUIState()

    data class Content(val data: HistoryDetail) : HistoryDetailUIState()
}
