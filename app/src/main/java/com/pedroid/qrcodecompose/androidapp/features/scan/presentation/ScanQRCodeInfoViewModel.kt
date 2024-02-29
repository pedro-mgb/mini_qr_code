package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.presentation.ExternalAppStartResponse
import com.pedroid.qrcodecompose.androidapp.core.presentation.getErrorMessageKey
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ScanQRCodeInfoViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(QRCodeInfoUIState())
        val uiState: StateFlow<QRCodeInfoUIState>
            get() = _uiState

        fun onNewAction(action: QRCodeInfoUIAction) {
            when (action) {
                is QRCodeInfoUIAction.CodeReceived -> {
                    if (action.qrCode.isNullOrEmpty()) {
                        _uiState.update {
                            it.copy(content = QRCodeInfoContentUIState.Initial)
                        }
                    } else {
                        _uiState.update {
                            it.copy(content = QRCodeInfoContentUIState.CodeScanned(action.qrCode))
                        }
                    }
                }

                is QRCodeInfoUIAction.AppStarted -> {
                    action.response.getErrorMessageKey()?.let { stringKey ->
                        _uiState.update { it.copy(errorMessageKey = stringKey) }
                    }
                }

                is QRCodeInfoUIAction.ErrorShown -> {
                    _uiState.update { it.copy(errorMessageKey = "") }
                }
            }
        }
    }

data class QRCodeInfoUIState(
    val content: QRCodeInfoContentUIState = QRCodeInfoContentUIState.Initial,
    val errorMessageKey: String = "",
)

sealed interface QRCodeInfoContentUIState {
    data object Initial : QRCodeInfoContentUIState

    data class CodeScanned(val qrCode: String) : QRCodeInfoContentUIState
}

sealed interface QRCodeInfoUIAction {
    data class CodeReceived(val qrCode: String?) : QRCodeInfoUIAction

    data class AppStarted(val response: ExternalAppStartResponse) : QRCodeInfoUIAction

    data object ErrorShown : QRCodeInfoUIAction
}
