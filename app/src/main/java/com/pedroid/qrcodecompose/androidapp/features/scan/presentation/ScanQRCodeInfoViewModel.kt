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
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
        private val logger: Logger,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QRCodeInfoUIState())
        val uiState: StateFlow<QRCodeInfoUIState>
            get() = _uiState

        private val newQRActionFlow = MutableStateFlow(ScannedCodeWithAction())

        init {
            setupSaveHistory()
        }

        private fun setupSaveHistory() {
            uiState
                .map { (it.content as? QRCodeInfoContentUIState.CodeScanned)?.qrCode }
                .combine(newQRActionFlow) { scannedCode, actionForCurrentScannedCode ->
                    logger.debug(LOG_TAG, "Processing to check if should save history - $scannedCode / $actionForCurrentScannedCode")
                    when {
                        scannedCode == null || scannedCode.data.isBlank() -> null
                        actionForCurrentScannedCode.actionComplete?.status != ActionStatus.SUCCESS -> null
                        scannedCode == actionForCurrentScannedCode.code -> null
                        else -> ScannedCodeWithAction(scannedCode, actionForCurrentScannedCode.actionComplete)
                    }
                }
                .filterNotNull()
                .onEach { newCodeWithAction ->
                    newQRActionFlow.update {
                        it.copy(code = newCodeWithAction.code, actionComplete = null)
                    }
                    logger.debug(LOG_TAG, "Saving to history based on action - $newCodeWithAction")
                    historyRepository.saveScannedCode(newCodeWithAction.code)
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
