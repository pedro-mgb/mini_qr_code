package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeCustomizationOptions
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeGeneratingContent
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateQRCodeViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private lateinit var sut: GenerateQRCodeViewModel

    @Before
    fun setUp() {
        sut =
            GenerateQRCodeViewModel(
                savedStateHandle = SavedStateHandle(),
                logger = mockk(relaxed = true),
                generateMessageFactory =
                    mockk {
                        every { createGenerateErrorMessage(any(), any()) } returns ERROR_MESSAGE
                    },
            )
    }

    @Test
    fun `given sut is created, state has empty values`() {
        assertEquals(
            GenerateQRCodeUIState(
                content = GenerateQRCodeContentState(inputText = ""),
                temporaryMessage = null,
            ),
            sut.uiState.value,
        )
    }

    @Test
    fun `given update text action is sent, state text is updated`() =
        runTest {
            sut.uiState.test {
                assertEquals("", awaitItem().content.inputText) // Initial state
                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("newText"))
                assertEquals("newText", awaitItem().content.inputText)
            }
        }

    @Test
    fun `given multiple update text actions are sent with less than 400 ms, only input text is updated in state`() =
        runTest {
            sut.uiState.test {
                // Initial state
                assertEquals(
                    DEFAULT_CONTENT_STATE,
                    awaitItem().content,
                )

                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SHOULD_NOT_EMIT_QR_CODE_1"))
                testScheduler.advanceTimeBy(100L)
                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SHOULD_NOT_EMIT_QR_CODE_2"))
                testScheduler.advanceTimeBy(100L)

                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SHOULD_NOT_EMIT_QR_CODE_1",
                        generating = QRCodeGeneratingContent(qrCodeText = ""),
                    ),
                    awaitItem().content,
                )
                val lastItem = awaitItem()
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SHOULD_NOT_EMIT_QR_CODE_2",
                        generating = QRCodeGeneratingContent(qrCodeText = ""),
                    ),
                    lastItem.content,
                )
                assertFalse(lastItem.content.canGenerate)
                expectNoEvents()
            }
        }

    @Test
    fun `given multiple update text actions are sent with more than 400 ms, input text and qr code text are updated in state`() =
        runTest {
            sut.uiState.test {
                // Initial state
                assertEquals(
                    DEFAULT_CONTENT_STATE,
                    awaitItem().content,
                )

                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("FIRST_EMISSION"))
                testScheduler.advanceTimeBy(401L)
                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SECOND_EMISSION"))

                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "FIRST_EMISSION",
                        generating = QRCodeGeneratingContent(qrCodeText = ""),
                    ),
                    awaitItem().content,
                )
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "FIRST_EMISSION",
                        generating = QRCodeGeneratingContent(qrCodeText = "FIRST_EMISSION"),
                    ),
                    awaitItem().content,
                )
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SECOND_EMISSION",
                        generating = QRCodeGeneratingContent(qrCodeText = "FIRST_EMISSION"),
                    ),
                    awaitItem().content,
                )
                val lastItem = awaitItem()
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SECOND_EMISSION",
                        generating = QRCodeGeneratingContent(qrCodeText = "SECOND_EMISSION"),
                    ),
                    lastItem.content,
                )
                assertTrue(lastItem.content.canGenerate)
                expectNoEvents()
            }
        }

    @Test
    fun `given format change action is sent, new state is emitted`() =
        runTest {
            sut.onNewAction(
                GenerateQRCodeUIAction.Customize(
                    QRCodeCustomizationOptions(
                        format = QRCodeComposeXFormat.DATA_MATRIX,
                    ),
                ),
            )

            assertEquals(
                GenerateQRCodeContentState(
                    inputText = "",
                    generating =
                        QRCodeGeneratingContent(
                            qrCodeText = "",
                            format = QRCodeComposeXFormat.DATA_MATRIX,
                        ),
                ),
                sut.uiState.value.content,
            )
        }

    @Test
    fun `given update text action with length not bigger than format max length, new state is emitted`() {
        runTest {
            val format = QRCodeComposeXFormat.BARCODE_128
            val validText = "1".repeat(format.maxLength)
            sut.onNewAction(
                GenerateQRCodeUIAction.Customize(
                    QRCodeCustomizationOptions(
                        format = format,
                    ),
                ),
            )
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.UpdateText(validText))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            assertEquals(
                GenerateQRCodeContentState(
                    inputText = validText,
                    generating =
                        QRCodeGeneratingContent(
                            qrCodeText = validText,
                            format = format,
                        ),
                ),
                sut.uiState.value.content,
            )
        }
    }

    @Test
    fun `given update text action with length is bigger than format max length, no new state is emitted`() =
        runTest {
            val format = QRCodeComposeXFormat.BARCODE_128
            val textTooLong = "1".repeat(format.maxLength + 1)
            sut.onNewAction(
                GenerateQRCodeUIAction.Customize(
                    QRCodeCustomizationOptions(
                        format = format,
                    ),
                ),
            )
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.UpdateText(textTooLong))
            testScheduler.advanceTimeBy(1000L)

            assertEquals(
                GenerateQRCodeContentState(
                    inputText = "",
                    generating =
                        QRCodeGeneratingContent(
                            qrCodeText = "",
                            format = format,
                        ),
                ),
                sut.uiState.value.content,
            )
        }

    @Test
    fun `given update text action with invalid text for specific format, error message is emitted in state`() =
        runTest {
            val format = QRCodeComposeXFormat.BARCODE_128
            val invalidText = "€".repeat(format.maxLength - 1)
            sut.onNewAction(
                GenerateQRCodeUIAction.Customize(
                    QRCodeCustomizationOptions(
                        format = format,
                    ),
                ),
            )
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.UpdateText(invalidText))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            assertEquals(
                GenerateQRCodeContentState(
                    inputText = invalidText,
                    inputErrorMessage = ERROR_MESSAGE,
                    generating =
                        QRCodeGeneratingContent(
                            qrCodeText = "",
                            format = format,
                        ),
                ),
                sut.uiState.value.content,
            )
            assertFalse(sut.uiState.value.content.canGenerate)
            assertTrue(sut.uiState.value.content.inputError)
        }

    @Test
    fun `given format change action is done but text is invalid for new format, error is emitted in state`() =
        runTest {
            val invalidInput = "abcde"
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText(invalidInput))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(
                GenerateQRCodeUIAction.Customize(
                    QRCodeCustomizationOptions(
                        format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                    ),
                ),
            )
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            // the qrCodeText remains with current value
            //  in the event that the user switches from valid format to invalid format then back to valid
            //  the qr code to generate is still there, and will be shown
            assertEquals(
                GenerateQRCodeContentState(
                    inputText = invalidInput,
                    inputErrorMessage = ERROR_MESSAGE,
                    generating =
                        QRCodeGeneratingContent(
                            qrCodeText = invalidInput,
                            format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                        ),
                ),
                sut.uiState.value.content,
            )
            assertFalse(sut.uiState.value.content.canGenerate)
        }

    @Test
    fun `given generate error actions are sent, state errorMessageKey is updated`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                sut.onNewAction(GenerateQRCodeUIAction.GenerateErrorReceived(IllegalStateException()))
                awaitItem().temporaryMessage.assertHasError()
                sut.onNewAction(GenerateQRCodeUIAction.TmpMessageShown)
                assertTrue(awaitItem().temporaryMessage == null)
            }
        }

    @Test
    fun `given image share error actions are sent, state errorMessageKey is updated`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                sut.onNewAction(
                    GenerateQRCodeUIAction.QRActionComplete(
                        QRAppActions.ShareApp(ActionStatus.ERROR_NO_APP),
                    ),
                )
                awaitItem().temporaryMessage.assertHasError()
                sut.onNewAction(GenerateQRCodeUIAction.TmpMessageShown)
                assertTrue(awaitItem().temporaryMessage == null)
            }
        }

    private fun TemporaryMessageData?.assertHasError() {
        assertTrue(this != null)
        assertTrue(this!!.text.isNotBlank())
        assertEquals(TemporaryMessageType.ERROR_SNACKBAR, this.type)
    }

    companion object {
        private const val ERROR_MESSAGE = "error message"

        private val DEFAULT_CONTENT_STATE =
            GenerateQRCodeContentState(
                inputText = "",
                generating = QRCodeGeneratingContent(),
            )
    }
}
