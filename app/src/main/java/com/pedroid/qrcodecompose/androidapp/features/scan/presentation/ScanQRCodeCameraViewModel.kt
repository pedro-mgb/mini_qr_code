package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsReadOnlyRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanQRCodeCameraViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsReadOnlyRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<QRCodeCameraUIState>(QRCodeCameraUIState.Idle)
        val uiState: StateFlow<QRCodeCameraUIState>
            get() = _uiState

        fun onNewAction(action: QRCodeCameraUIAction) {
            when (action) {
                is QRCodeCameraUIAction.ResultUpdate -> {
                    when (action.result) {
                        is QRCodeScanResult.Scanned -> {
                            viewModelScope.launch {
                                _uiState.value =
                                    QRCodeCameraUIState.ScanComplete(
                                        action.result.qrCode,
                                        action.result.format,
                                        vibrate = settingsRepository.getFullSettings().first().scan.hapticFeedback,
                                    )
                            }
                        }

                        else -> {
                            // not handling any other result type
                        }
                    }
                }
            }
        }
    }

sealed class QRCodeCameraUIState {
    data object Idle : QRCodeCameraUIState()

    data class ScanComplete(
        val qrCode: String,
        val format: QRCodeComposeXFormat,
        val vibrate: Boolean = false,
    ) : QRCodeCameraUIState()
}

sealed class QRCodeCameraUIAction {
    data class ResultUpdate(val result: QRCodeScanResult) : QRCodeCameraUIAction()
}
