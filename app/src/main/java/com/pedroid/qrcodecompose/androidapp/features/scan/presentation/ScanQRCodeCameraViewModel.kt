package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScanQRCodeCameraViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow<QRCodeCameraUIState>(QRCodeCameraUIState.Idle)
        val uiState: StateFlow<QRCodeCameraUIState>
            get() = _uiState

        fun onNewAction(action: QRCodeCameraUIAction) {
            when (action) {
                is QRCodeCameraUIAction.ResultUpdate -> {
                    when (action.result) {
                        is QRCodeScanResult.Scanned -> {
                            _uiState.value = QRCodeCameraUIState.ScanComplete(action.result.qrCode)
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

    data class ScanComplete(val qrCode: String) : QRCodeCameraUIState()
}

sealed class QRCodeCameraUIAction {
    data class ResultUpdate(val result: QRCodeScanResult) : QRCodeCameraUIAction()
}
