package com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.asTemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.core.presentation.getHourTimeFormat
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.getTypeUI
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val LOG_TAG = "HistoryDetailViewModel"

@HiltViewModel(assistedFactory = HistoryDetailViewModelFactory::class)
class HistoryDetailViewModel
    @AssistedInject
    constructor(
        @Assisted private val entryUid: Long,
        private val historyRepository: HistoryRepository,
        private val logger: Logger,
    ) : ViewModel() {
        private val generateResultMutableFlow = MutableStateFlow<QRCodeGenerateResult?>(null)

        private val temporaryMessageFlow = MutableStateFlow<TemporaryMessageData?>(null)

        private val originalVMFlow: Flow<Triple<HistoryEntry?, QRCodeGenerateResult?, TemporaryMessageData?>>
            get() =
                combine(
                    historyRepository.getSingleHistory(entryUid),
                    generateResultMutableFlow.asStateFlow(),
                    temporaryMessageFlow.asStateFlow(),
                ) { entry, generateResult, tmpMessage ->
                    Triple(entry, generateResult, tmpMessage)
                }

        val uiState: StateFlow<HistoryDetailUIState> =
            this.createStateFlow(
                originalFlow = originalVMFlow,
                initialValue = HistoryDetailUIState.Idle,
                mapper = ::mapToUIState,
            )

        private val defaultZoneId get() = ZoneId.systemDefault()
        private val defaultLocale get() = Locale.getDefault()
        private val dateTimePattern get() = "EEEE, d MMM yyyy - ${getHourTimeFormat()}"

        private fun mapToUIState(input: Triple<HistoryEntry?, QRCodeGenerateResult?, TemporaryMessageData?>): HistoryDetailUIState {
            val entry: HistoryEntry? = input.first
            val generateResult: QRCodeGenerateResult? = input.second
            val temporaryMessage: TemporaryMessageData? = input.third
            logger.debug(LOG_TAG, "history entry received - $entry")

            val state: HistoryDetailUIState =
                if (entry == null) {
                    HistoryDetailUIState.Deleted
                } else {
                    logger.debug(LOG_TAG, "result after generating qr code: $generateResult + with temporary message $temporaryMessage")
                    HistoryDetailUIState.Content(
                        data = entry.toHistoryDetail(generateResult),
                        temporaryMessage = temporaryMessage,
                    )
                }

            logger.debug(LOG_TAG, "history entry received - $entry")
            return state
        }

        private fun HistoryEntry.toHistoryDetail(result: QRCodeGenerateResult?): HistoryDetail =
            HistoryDetail(
                uid = this.uid,
                value = value,
                format = this.format,
                displayDate =
                    this.creationDate
                        .atZone(defaultZoneId)
                        .format(DateTimeFormatter.ofPattern(dateTimePattern, defaultLocale)),
                typeUI = this.getTypeUI(),
                errorInGenerating = result is QRCodeGenerateResult.Error,
            )

        fun onNewAction(action: HistoryDetailUIAction) {
            viewModelScope.launch {
                logger.debug(LOG_TAG, "new action - $action")
                when (action) {
                    is HistoryDetailUIAction.Generate -> {
                        generateResultMutableFlow.emit(action.generateResult)
                    }
                    is HistoryDetailUIAction.QRActionComplete -> {
                        temporaryMessageFlow.emit(action.action.asTemporaryMessage())
                    }
                    is HistoryDetailUIAction.TmpMessageShown -> {
                        temporaryMessageFlow.emit(null)
                    }
                }
            }
        }
    }

@AssistedFactory
interface HistoryDetailViewModelFactory {
    fun create(entryUid: Long): HistoryDetailViewModel
}

sealed class HistoryDetailUIState {
    data object Idle : HistoryDetailUIState()

    data object Deleted : HistoryDetailUIState()

    data class Content(
        val data: HistoryDetail,
        val temporaryMessage: TemporaryMessageData? = null,
    ) : HistoryDetailUIState()
}

sealed class HistoryDetailUIAction {
    data class Generate(val generateResult: QRCodeGenerateResult) : HistoryDetailUIAction()

    data class QRActionComplete(val action: QRAppActions) : HistoryDetailUIAction()

    data object TmpMessageShown : HistoryDetailUIAction()
}
