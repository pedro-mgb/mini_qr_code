package com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsReadOnlyRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "ScanQRCodeFromFile"

@HiltViewModel
class ScanQRCodeFromFileViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsReadOnlyRepository,
        private val logger: Logger,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<QRCodeFromFileUIState>(QRCodeFromFileUIState.Init)
        val uiState: StateFlow<QRCodeFromFileUIState>
            get() = _uiState.asStateFlow()

        fun onNewAction(action: QRCodeFromFileUIAction) {
            logger.debug(LOG_TAG, "new action $action")
            viewModelScope.launch {
                if (action is QRCodeFromFileUIAction.ScanResult) {
                    action.result.toUIState()?.let {
                        _uiState.value = it
                    }
                } else if (action is QRCodeFromFileUIAction.StartFileSelection) {
                    _uiState.value = QRCodeFromFileUIState.Loading
                }
            }
        }

        private suspend fun QRCodeScanResult.toUIState(): QRCodeFromFileUIState? {
            return when (this) {
                QRCodeScanResult.Cancelled -> {
                    if (uiState.value is QRCodeFromFileUIState.InitialState) {
                        QRCodeFromFileUIState.Cancelled
                    } else {
                        null
                    }
                }

                QRCodeScanResult.Invalid,
                QRCodeScanResult.UnrecoverableError,
                -> {
                    QRCodeFromFileUIState.Error
                }

                is QRCodeScanResult.Scanned -> {
                    QRCodeFromFileUIState.Success(
                        qrCode,
                        format,
                        vibrate = settingsRepository.getFullSettings().first().scan.hapticFeedback,
                    )
                }

                else -> {
                    // no new state to emit
                    null
                }
            }
        }
    }

sealed class QRCodeFromFileUIState {
    sealed class InitialState : QRCodeFromFileUIState()

    sealed class TerminalState : QRCodeFromFileUIState()

    data object Init : InitialState()

    data object Loading : InitialState()

    data object Cancelled : TerminalState()

    data object Error : TerminalState()

    data class Success(
        val qrCode: String,
        val format: QRCodeComposeXFormat,
        val vibrate: Boolean = false,
    ) : TerminalState()
}

sealed class QRCodeFromFileUIAction {
    data object StartFileSelection : QRCodeFromFileUIAction()

    data class ScanResult(val result: QRCodeScanResult) : QRCodeFromFileUIAction()
}
