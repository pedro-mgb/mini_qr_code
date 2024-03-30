package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.presentation.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.presentation.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeCustomizationOptions
import com.pedroid.qrcodecompose.androidapp.testutils.CoroutineDispatcherTestRule
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SHOULD_NOT_EMIT_QR_CODE_2",
                        generating = QRCodeGeneratingContent(qrCodeText = ""),
                    ),
                    awaitItem().content,
                )
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
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SECOND_EMISSION",
                        generating = QRCodeGeneratingContent(qrCodeText = "SECOND_EMISSION"),
                    ),
                    awaitItem().content,
                )
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
        val DEFAULT_CONTENT_STATE =
            GenerateQRCodeContentState(
                inputText = "",
                generating = QRCodeGeneratingContent(),
            )
    }
}
