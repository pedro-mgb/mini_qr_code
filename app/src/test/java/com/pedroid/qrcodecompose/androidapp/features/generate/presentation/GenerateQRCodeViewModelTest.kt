package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeCustomizationOptions
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeGeneratingContent
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsReadOnlyRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val historyRepository =
        mockk<HistoryRepository> {
            coEvery { addHistoryEntry(any()) } returns 2L
        }

    private val settingsMutableFlow = MutableStateFlow(FullSettings())
    private val settingsRepository =
        mockk<SettingsReadOnlyRepository> {
            coEvery { getFullSettings() } returns settingsMutableFlow.asStateFlow()
        }

    private lateinit var sut: GenerateQRCodeViewModel

    @Before
    fun setUp() {
        sut =
            GenerateQRCodeViewModel(
                historyRepository = historyRepository,
                settingsRepository = settingsRepository,
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

    @Test
    fun `given action success on generated qr code, the qr code is saved to history`() =
        runTest {
            val qrCodeTextAction = GenerateQRCodeUIAction.UpdateText("qr code")
            sut.onNewAction(qrCodeTextAction)
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            val entrySlot = slot<HistoryEntry.Generate>()

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(capture(entrySlot))
            }
            val capturedEntry = entrySlot.captured
            assertEquals(capturedEntry.value, "qr code")
            assertEquals(capturedEntry.format, QRCodeComposeXFormat.QR_CODE)
        }

    @Test
    fun `given 2 action success on same code, only saved to history once`() =
        runTest {
            val qrCodeTextAction = GenerateQRCodeUIAction.UpdateText("qr code")
            sut.onNewAction(qrCodeTextAction)
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given error on action, not saved to history`() =
        runTest {
            val qrCodeTextAction = GenerateQRCodeUIAction.UpdateText("qr code")
            sut.onNewAction(qrCodeTextAction)
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.ERROR_FILE)))

            coVerify(inverse = true) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given action with empty content, not saved to history`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.SUCCESS)))

            coVerify(inverse = true) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given 2 actions with different typed texts, saved to history only once`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("qr code 1"))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("qr code 2"))
            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given 2 actions with different generated text, saved to history twice`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("qr code 1"))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("qr code 2"))
            // advancing time to make sure current actions have been processed
            //  this is the difference between this and the above test
            testScheduler.advanceTimeBy(1000L)
            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))
            val list = mutableListOf<HistoryEntry.Generate>()

            coVerify(exactly = 2) {
                historyRepository.addHistoryEntry(capture(list))
            }
            assertEquals(list.map { it.value }, listOf("qr code 1", "qr code 2"))
        }

    @Test
    fun `given 2 actions with different code format, saved to history twice`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("some code"))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)
            val newFormat = QRCodeComposeXFormat.AZTEC

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(GenerateQRCodeUIAction.Customize(QRCodeCustomizationOptions(newFormat)))
            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))
            val list = mutableListOf<HistoryEntry.Generate>()

            coVerify(exactly = 2) {
                historyRepository.addHistoryEntry(capture(list))
            }
            assertEquals(list.map { it.format }, listOf(QRCodeComposeXFormat.QR_CODE, newFormat))
        }

    @Test
    fun `given action with generated code with history settings save off, not saved to history`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("some code"))
            settingsMutableFlow.emit(FullSettings(generate = GenerateSettings(historySave = HistorySavePreferences.NEVER_SAVE)))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.SUCCESS)))

            coVerify(inverse = true) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given action with generated code before history settings save on, not saved to history`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("some code"))
            settingsMutableFlow.emit(FullSettings(generate = GenerateSettings(historySave = HistorySavePreferences.NEVER_SAVE)))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.SUCCESS)))
            settingsMutableFlow.emit(FullSettings(generate = GenerateSettings(historySave = HistorySavePreferences.UPON_USER_ACTION)))

            coVerify(inverse = true) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given action with generated code after history settings save on, code is saved to history`() =
        runTest {
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("some code"))
            settingsMutableFlow.emit(FullSettings(generate = GenerateSettings(historySave = HistorySavePreferences.NEVER_SAVE)))
            // advancing time to make sure current actions have been processed
            testScheduler.advanceTimeBy(1000L)

            settingsMutableFlow.emit(FullSettings(generate = GenerateSettings(historySave = HistorySavePreferences.UPON_USER_ACTION)))
            sut.onNewAction(GenerateQRCodeUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.SUCCESS)))

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(any())
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
