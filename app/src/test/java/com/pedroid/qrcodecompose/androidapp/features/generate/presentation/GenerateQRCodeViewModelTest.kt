package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.presentation.AppResponseStatus
import com.pedroid.qrcodecompose.androidapp.core.presentation.ExternalAppStartResponse
import com.pedroid.qrcodecompose.androidapp.testutils.CoroutineDispatcherTestRule
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
                errorMessageKey = "",
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
                    GenerateQRCodeContentState(inputText = "", qrCodeText = ""),
                    awaitItem().content,
                )

                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SHOULD_NOT_EMIT_QR_CODE_1"))
                testScheduler.advanceTimeBy(100L)
                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SHOULD_NOT_EMIT_QR_CODE_2"))
                testScheduler.advanceTimeBy(100L)

                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SHOULD_NOT_EMIT_QR_CODE_1",
                        qrCodeText = "",
                    ),
                    awaitItem().content,
                )
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SHOULD_NOT_EMIT_QR_CODE_2",
                        qrCodeText = "",
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
                    GenerateQRCodeContentState(inputText = "", qrCodeText = ""),
                    awaitItem().content,
                )

                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("FIRST_EMISSION"))
                testScheduler.advanceTimeBy(401L)
                sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SECOND_EMISSION"))

                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "FIRST_EMISSION",
                        qrCodeText = "",
                    ),
                    awaitItem().content,
                )
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "FIRST_EMISSION",
                        qrCodeText = "FIRST_EMISSION",
                    ),
                    awaitItem().content,
                )
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SECOND_EMISSION",
                        qrCodeText = "FIRST_EMISSION",
                    ),
                    awaitItem().content,
                )
                assertEquals(
                    GenerateQRCodeContentState(
                        inputText = "SECOND_EMISSION",
                        qrCodeText = "SECOND_EMISSION",
                    ),
                    awaitItem().content,
                )
                expectNoEvents()
            }
        }

    @Test
    fun `given generate error actions are sent, state errorMessageKey is updated`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                sut.onNewAction(GenerateQRCodeUIAction.GenerateErrorReceived(IllegalStateException()))
                assertTrue(awaitItem().errorMessageKey.isNotBlank())
                sut.onNewAction(GenerateQRCodeUIAction.ErrorShown)
                assertTrue(awaitItem().errorMessageKey.isEmpty())
            }
        }

    @Test
    fun `given file save error actions are sent, state errorMessageKey is updated`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                sut.onNewAction(GenerateQRCodeUIAction.ErrorSavingToFile)
                assertTrue(awaitItem().errorMessageKey.isNotBlank())
                sut.onNewAction(GenerateQRCodeUIAction.ErrorShown)
                assertTrue(awaitItem().errorMessageKey.isEmpty())
            }
        }

    @Test
    fun `given image share error actions are sent, state errorMessageKey is updated`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                sut.onNewAction(
                    GenerateQRCodeUIAction.AppStarted(
                        ExternalAppStartResponse.ShareApp(AppResponseStatus.ERROR_NO_APP),
                    ),
                )
                assertTrue(awaitItem().errorMessageKey.isNotBlank())
                sut.onNewAction(GenerateQRCodeUIAction.ErrorShown)
                assertTrue(awaitItem().errorMessageKey.isEmpty())
            }
        }
}
