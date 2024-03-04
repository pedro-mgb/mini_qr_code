package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.ExternalAppStartResponse
import com.pedroid.qrcodecompose.androidapp.core.presentation.getErrorMessageKey
import com.pedroid.qrcodecompose.androidapp.core.presentation.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

private const val LOG_TAG = "GenerateQRCode"
private const val GENERATE_UI_STATE_KEY: String = "GENERATE_QR_CODE_UI_STATE"

// time in milliseconds to wait until there is no additional typing, and start generating code
private const val AWAIT_INPUT_STOP_INTERVAL = 400L

@HiltViewModel
class GenerateQRCodeViewModel
    @Inject
    constructor(
        private val savedStateHandle: SavedStateHandle,
        private val logger: Logger,
    ) : ViewModel() {
        val uiState: StateFlow<GenerateQRCodeUIState> =
            savedStateHandle.getStateFlow(
                key = GENERATE_UI_STATE_KEY,
                initialValue = GenerateQRCodeUIState(GenerateQRCodeContentState(), ""),
            )

        private val updateTextActionFlow: MutableStateFlow<GenerateQRCodeUIAction.UpdateText> =
            MutableStateFlow(GenerateQRCodeUIAction.UpdateText(""))

        init {
            setupUpdateTextAction()
        }

        @OptIn(FlowPreview::class)
        private fun setupUpdateTextAction() {
            updateTextActionFlow
                .asStateFlow()
                .onEach { action ->
                    logger.debug(LOG_TAG, "Received action $action")
                    savedStateHandle.updateState {
                        it?.copy(content = it.content.copy(inputText = action.text))
                    }
                }
                .debounce(AWAIT_INPUT_STOP_INTERVAL)
                .onEach { action ->
                    logger.debug(LOG_TAG, "Update QR code to generate based on action $action")
                    // only update qrcode to generate if user as stopped typing for a small interval
                    savedStateHandle.updateState {
                        it?.copy(content = it.content.copy(qrCodeText = action.text))
                    }
                }
                .launchIn(viewModelScope)
        }

        fun onNewAction(action: GenerateQRCodeUIAction) {
            viewModelScope.launch {
                when (action) {
                    is GenerateQRCodeUIAction.UpdateText -> {
                        updateTextActionFlow.emit(action)
                    }

                    is GenerateQRCodeUIAction.GenerateErrorReceived -> {
                        logger.error(LOG_TAG, "Error in generating qr code", action.exception)
                        savedStateHandle.updateState {
                            it?.copy(errorMessageKey = "generate_code_error")
                        }
                    }

                    is GenerateQRCodeUIAction.AppStarted -> {
                        action.response.getErrorMessageKey()?.let { errorStringKey ->
                            savedStateHandle.updateState {
                                it?.copy(errorMessageKey = errorStringKey)
                            }
                        }
                    }

                    GenerateQRCodeUIAction.ErrorSavingToFile -> {
                        logger.error(LOG_TAG, "Error in saving qr code to file")
                        savedStateHandle.updateState {
                            it?.copy(errorMessageKey = "code_saved_to_file_success")
                        }
                    }

                    GenerateQRCodeUIAction.ErrorShown -> {
                        savedStateHandle.updateState {
                            it?.copy(errorMessageKey = "")
                        }
                    }
                }
            }
        }

        private fun SavedStateHandle.updateState(updateDelegate: (GenerateQRCodeUIState?) -> GenerateQRCodeUIState?) {
            update(GENERATE_UI_STATE_KEY, updateDelegate)
        }
    }

@Parcelize
data class GenerateQRCodeUIState(
    val content: GenerateQRCodeContentState = GenerateQRCodeContentState(),
    val errorMessageKey: String = "",
) : Parcelable

@Parcelize
data class GenerateQRCodeContentState(
    val inputText: String = "",
    val qrCodeText: String = "",
) : Parcelable

sealed interface GenerateQRCodeUIAction {
    data class UpdateText(val text: String) : GenerateQRCodeUIAction

    data class GenerateErrorReceived(val exception: Exception) : GenerateQRCodeUIAction

    data class AppStarted(val response: ExternalAppStartResponse) : GenerateQRCodeUIAction

    data object ErrorSavingToFile : GenerateQRCodeUIAction

    data object ErrorShown : GenerateQRCodeUIAction
}
