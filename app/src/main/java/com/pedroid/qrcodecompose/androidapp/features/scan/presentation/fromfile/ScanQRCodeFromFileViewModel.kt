package com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val LOG_TAG = "ScanQRCodeFromFile"

@HiltViewModel
class ScanQRCodeFromFileViewModel
    @Inject
    constructor(
        private val logger: Logger,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<QRCodeFromFileUIState>(QRCodeFromFileUIState.Loading)
        val uiState: StateFlow<QRCodeFromFileUIState>
            get() = _uiState.asStateFlow()

        fun onNewAction(action: QRCodeFromFileUIAction) {
            logger.debug(LOG_TAG, "new action $action")
            if (action is QRCodeFromFileUIAction.ScanResult) {
                action.result.toUIState()?.let {
                    _uiState.value = it
                }
            }
        }

        private fun QRCodeScanResult.toUIState(): QRCodeFromFileUIState? {
            return when (this) {
                QRCodeScanResult.Cancelled -> {
                    QRCodeFromFileUIState.Cancelled
                }

                QRCodeScanResult.Invalid,
                QRCodeScanResult.UnrecoverableError,
                -> {
                    QRCodeFromFileUIState.Error
                }

                is QRCodeScanResult.Scanned -> {
                    QRCodeFromFileUIState.Success(qrCode)
                }

                else -> {
                    // no new state to emit
                    null
                }
            }
        }
    }

sealed class QRCodeFromFileUIState {
    data object Loading : QRCodeFromFileUIState()

    data object Cancelled : QRCodeFromFileUIState()

    data object Error : QRCodeFromFileUIState()

    data class Success(val qrCode: String) : QRCodeFromFileUIState()
}

sealed class QRCodeFromFileUIAction {
    data class ScanResult(val result: QRCodeScanResult) : QRCodeFromFileUIAction()
}
