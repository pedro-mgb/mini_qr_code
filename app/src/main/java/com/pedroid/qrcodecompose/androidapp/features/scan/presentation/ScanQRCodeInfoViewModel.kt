package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.asTemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScanSource
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecompose.androidapp.features.scan.data.saveScannedCode
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsReadOnlyRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val LOG_TAG = "ScanQRCodeInfoVM"

@HiltViewModel
class ScanQRCodeInfoViewModel
    @Inject
    constructor(
        private val historyRepository: HistoryRepository,
        private val settingsRepository: SettingsReadOnlyRepository,
        private val logger: Logger,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QRCodeInfoUIState())
        val uiState: StateFlow<QRCodeInfoUIState>
            get() = _uiState

        private val newQRActionFlow = MutableStateFlow(ScannedCodeWithAction())

        init {
            setupSaveHistory()
            setupSettings()
        }

        private fun setupSaveHistory() {
            combine(
                uiState.map { (it.content as? QRCodeInfoContentUIState.CodeScanned)?.qrCode },
                newQRActionFlow,
                settingsRepository.getFullSettings().map { it.scan.historySave },
            ) { scannedCode, actionForCurrentScannedCode, savePreferences ->
                logger.debug(
                    LOG_TAG,
                    "Processing to check if should save history - $scannedCode / $actionForCurrentScannedCode / $savePreferences",
                )
                when {
                    scannedCode == null || scannedCode.data.isBlank() -> null
                    savePreferences == HistorySavePreferences.NEVER_SAVE -> {
                        resetActionComplete(actionForCurrentScannedCode)
                        null
                    }
                    actionForCurrentScannedCode.actionComplete?.status != ActionStatus.SUCCESS -> null
                    scannedCode == actionForCurrentScannedCode.code -> null
                    else -> ScannedCodeWithAction(scannedCode, actionForCurrentScannedCode.actionComplete)
                }
            }
                .filterNotNull()
                .onEach { newCodeWithAction ->
                    resetActionComplete(newCodeWithAction)
                    logger.debug(LOG_TAG, "Saving to history based on action - $newCodeWithAction")
                    historyRepository.saveScannedCode(newCodeWithAction.code)
                }
                .launchIn(viewModelScope)
        }

        private fun resetActionComplete(actionWithCode: ScannedCodeWithAction) {
            // reset the action flow to avoid repeated emissions and repeated saves
            newQRActionFlow.update {
                it.copy(code = actionWithCode.code, actionComplete = null)
            }
        }

        private fun setupSettings() {
            settingsRepository
                .getFullSettings()
                .distinctUntilChanged()
                .map { it.general.openUrlPreferences }
                .onEach { mode ->
                    _uiState.update {
                        it.copy(openUrlMode = mode)
                    }
                }
                .launchIn(viewModelScope)
        }

        fun onNewAction(action: QRCodeInfoUIAction) {
            logger.debug(LOG_TAG, "onNewAction -> $action")
            when (action) {
                is QRCodeInfoUIAction.CodeReceived -> {
                    if (action.qrCode == null || action.qrCode.data.isEmpty()) {
                        _uiState.update {
                            it.copy(content = QRCodeInfoContentUIState.Initial)
                        }
                    } else {
                        _uiState.update {
                            it.copy(content = QRCodeInfoContentUIState.CodeScanned(action.qrCode))
                        }
                    }
                }

                is QRCodeInfoUIAction.QRActionComplete -> {
                    newQRActionFlow.update {
                        it.copy(actionComplete = action.action)
                    }
                    action.action.asTemporaryMessage()?.let { tmpMessage ->
                        _uiState.update { it.copy(temporaryMessage = tmpMessage) }
                    }
                }

                is QRCodeInfoUIAction.TmpMessageShown -> {
                    _uiState.update { it.copy(temporaryMessage = null) }
                }
            }
        }
    }

data class QRCodeInfoUIState(
    val content: QRCodeInfoContentUIState = QRCodeInfoContentUIState.Initial,
    val openUrlMode: OpenUrlPreferences = OpenUrlPreferences.DEFAULT,
    val temporaryMessage: TemporaryMessageData? = null,
)

sealed class QRCodeInfoContentUIState {
    data object Initial : QRCodeInfoContentUIState()

    data class CodeScanned(val qrCode: ScannedCode) : QRCodeInfoContentUIState()
}

sealed class QRCodeInfoUIAction {
    data class CodeReceived(val qrCode: ScannedCode?) : QRCodeInfoUIAction()

    data class QRActionComplete(val action: QRAppActions) : QRCodeInfoUIAction()

    data object TmpMessageShown : QRCodeInfoUIAction()
}

private data class ScannedCodeWithAction(
    val code: ScannedCode = ScannedCode("", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA),
    val actionComplete: QRAppActions? = null,
)
