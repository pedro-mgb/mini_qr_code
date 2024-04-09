package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.asTemporaryMessage
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

private const val LOG_TAG = "ScanQRCodeInfoVM"

@HiltViewModel
class ScanQRCodeInfoViewModel
    @Inject
    constructor(
        private val logger: Logger,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QRCodeInfoUIState())
        val uiState: StateFlow<QRCodeInfoUIState>
            get() = _uiState

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
