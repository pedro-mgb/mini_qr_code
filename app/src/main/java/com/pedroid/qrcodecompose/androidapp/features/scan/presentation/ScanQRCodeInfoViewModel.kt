package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ScanQRCodeInfoViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow<QRCodeInfoUIState>(QRCodeInfoUIState.Initial)
    val uiState: StateFlow<QRCodeInfoUIState>
        get() = _uiState

    fun onNewAction(action: QRCodeInfoUIAction) {
        when (action) {
            is QRCodeInfoUIAction.CodeReceived -> {
                if (action.qrCode.isNullOrEmpty()) {
                    _uiState.value = QRCodeInfoUIState.Initial
                } else {
                    _uiState.value = QRCodeInfoUIState.CodeScanned(action.qrCode)
                }
            }
        }
    }
}

sealed interface QRCodeInfoUIState {
    data object Initial : QRCodeInfoUIState
    data class CodeScanned(val qrCode: String) : QRCodeInfoUIState
}

sealed interface QRCodeInfoUIAction {
    data class CodeReceived(val qrCode: String?) : QRCodeInfoUIAction
}