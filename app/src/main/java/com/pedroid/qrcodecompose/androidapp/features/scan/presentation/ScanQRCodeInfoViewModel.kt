package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.presentation.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.asTemporaryMessage
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

                is QRCodeInfoUIAction.QRActionComplete -> {
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

sealed interface QRCodeInfoContentUIState {
    data object Initial : QRCodeInfoContentUIState

    data class CodeScanned(val qrCode: String) : QRCodeInfoContentUIState
}

sealed interface QRCodeInfoUIAction {
    data class CodeReceived(val qrCode: String?) : QRCodeInfoUIAction

    data class QRActionComplete(val action: QRAppActions) : QRCodeInfoUIAction

    data object TmpMessageShown : QRCodeInfoUIAction
}
