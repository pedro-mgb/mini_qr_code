package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.asTemporaryMessage
import com.pedroid.qrcodecompose.androidapp.core.presentation.update
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeCustomizationOptions
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.generate.QRCodeComposeXNotCompliantWithFormatException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.IgnoredOnParcel
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
        private val generateMessageFactory: GenerateMessageFactory,
    ) : ViewModel() {
        val uiState: StateFlow<GenerateQRCodeUIState> =
            savedStateHandle.getStateFlow(
                key = GENERATE_UI_STATE_KEY,
                initialValue = GenerateQRCodeUIState(GenerateQRCodeContentState(), null),
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
                .map { action ->
                    logger.debug(LOG_TAG, "Received action $action")
                    val format = uiState.value.content.generating.format
                    val addingChars: Boolean = action.text.length > uiState.value.content.inputText.length
                    if (addingChars && action.text.length > format.maxLength) {
                        logger.debug(LOG_TAG, "Text exceeds length ${format.maxLength}, not updating state")
                        null
                    } else if (action.text.isNotEmpty() && !format.validationRegex.matches(action.text)) {
                        logger.debug(LOG_TAG, "Text is not valid according to regex, updating state with error")
                        val errorMessage = generateMessageFactory.createGenerateErrorMessage(format, action.text)
                        savedStateHandle.updateState {
                            it?.copy(
                                content =
                                    it.content.copy(
                                        inputText = action.text,
                                        inputErrorMessage = errorMessage,
                                    ),
                            )
                        }
                        null
                    } else {
                        logger.debug(LOG_TAG, "Receiving valid text, update input text state")
                        savedStateHandle.updateState {
                            it?.copy(content = it.content.copy(inputText = action.text, inputErrorMessage = ""))
                        }
                        action
                    }
                }
                .filterNotNull()
                .debounce(AWAIT_INPUT_STOP_INTERVAL)
                .onEach { action ->
                    // only update qrcode to generate if user as stopped typing for a small interval
                    logger.debug(LOG_TAG, "Update QR code to generate based on action $action")
                    savedStateHandle.updateState {
                        it?.copy(
                            content =
                                it.content.copy(
                                    generating =
                                        it.content.generating.copy(
                                            qrCodeText = action.text,
                                        ),
                                ),
                        )
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

                    is GenerateQRCodeUIAction.Customize -> {
                        logger.debug(LOG_TAG, "Customize action - $action")
                        val format = action.options.format
                        if (format != uiState.value.content.generating.format) {
                            val currentText = uiState.value.content.generating.qrCodeText
                            val isValidWithNewFormat = textValidForNewFormat(currentText, format)
                            val errorMessage =
                                if (isValidWithNewFormat) {
                                    ""
                                } else {
                                    generateMessageFactory.createGenerateErrorMessage(format, currentText)
                                }
                            savedStateHandle.updateState {
                                it?.copy(
                                    content =
                                        it.content.copy(
                                            inputErrorMessage = errorMessage,
                                            generating = it.content.generating.copy(format = format),
                                        ),
                                )
                            }
                        }
                    }

                    is GenerateQRCodeUIAction.GenerateErrorReceived -> {
                        logger.error(LOG_TAG, "Error in generating qr code", action.exception)
                        if (action.exception is QRCodeComposeXNotCompliantWithFormatException) {
                            val errorMessage =
                                generateMessageFactory.createGenerateErrorMessage(
                                    uiState.value.content.generating.format,
                                    uiState.value.content.generating.qrCodeText,
                                )
                            savedStateHandle.updateState {
                                it?.copy(
                                    content =
                                        it.content.copy(
                                            inputErrorMessage = errorMessage,
                                        ),
                                )
                            }
                        } else {
                            savedStateHandle.updateState {
                                it?.copy(temporaryMessage = TemporaryMessageData.error("generate_code_error"))
                            }
                        }
                    }

                    is GenerateQRCodeUIAction.QRActionComplete -> {
                        action.action.asTemporaryMessage()?.let { temporaryMessage ->
                            savedStateHandle.updateState {
                                it?.copy(temporaryMessage = temporaryMessage)
                            }
                        }
                    }

                    GenerateQRCodeUIAction.TmpMessageShown -> {
                        savedStateHandle.updateState {
                            it?.copy(temporaryMessage = null)
                        }
                    }
                }
            }
        }

        private fun textValidForNewFormat(
            text: String,
            format: QRCodeComposeXFormat,
        ): Boolean {
            val notExceedingMaxLength = text.length < format.maxLength
            val matchesRegex = format.validationRegex.matches(text)
            return text.isEmpty() || (notExceedingMaxLength && matchesRegex)
        }

        private fun SavedStateHandle.updateState(updateDelegate: (GenerateQRCodeUIState?) -> GenerateQRCodeUIState?) {
            val updateWithLogging: (GenerateQRCodeUIState?) -> GenerateQRCodeUIState? = {
                updateDelegate(it).also {
                    logger.debug(LOG_TAG, "updated ui state = $it")
                }
            }

            update(GENERATE_UI_STATE_KEY, updateWithLogging)
        }
    }

@Parcelize
data class GenerateQRCodeUIState(
    val content: GenerateQRCodeContentState = GenerateQRCodeContentState(),
    val temporaryMessage: TemporaryMessageData? = null,
) : Parcelable

@Parcelize
data class GenerateQRCodeContentState(
    val inputText: String = "",
    val inputErrorMessage: String = "",
    val generating: QRCodeGeneratingContent = QRCodeGeneratingContent(),
) : Parcelable {
    @IgnoredOnParcel
    val inputError: Boolean = inputErrorMessage.isNotBlank()

    @IgnoredOnParcel
    val canGenerate: Boolean = !inputError && !generating.empty
}

@Parcelize
data class QRCodeGeneratingContent(
    val qrCodeText: String = "",
    val format: QRCodeComposeXFormat = QRCodeComposeXFormat.QR_CODE,
) : Parcelable {
    @IgnoredOnParcel
    val empty: Boolean = qrCodeText.isBlank()
}

sealed class GenerateQRCodeUIAction {
    sealed class UpdateCodeContentAction : GenerateQRCodeUIAction()

    data class UpdateText(val text: String) : UpdateCodeContentAction()

    data class Customize(val options: QRCodeCustomizationOptions) : UpdateCodeContentAction()

    data class GenerateErrorReceived(val exception: Exception) : GenerateQRCodeUIAction()

    data class QRActionComplete(val action: QRAppActions) : GenerateQRCodeUIAction()

    data object TmpMessageShown : GenerateQRCodeUIAction()
}
