package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.testutils.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GenerateQRCodeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var sut: GenerateQRCodeViewModel

    @Before
    fun setUp() {
        sut = GenerateQRCodeViewModel(
            savedStateHandle = SavedStateHandle()
        )
    }

    @Test
    fun `given sut is created, state has empty values`() {
        assertEquals(
            GenerateQRCodeUIState(
                content = GenerateQRCodeContentState(inputText = ""),
                errorMessageKey = ""
            ),
            sut.uiState.value
        )
    }

    @Test
    fun `given update text action is sent, state text is updated`() = runTest {
        sut.uiState.test {
            assertEquals("", awaitItem().content.inputText)  // Initial state
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("newText"))
            assertEquals("newText", awaitItem().content.inputText)
        }
    }

    @Test
    fun `given multiple update text actions are sent, state text is updated only if there is 1_000ms between them`() = runTest {
        sut.uiState.test {
            assertEquals(awaitItem().content.inputText, "")  // Initial state
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SHOULD_NOT_EMIT_1"))
            testScheduler.advanceTimeBy(100L)
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("SHOULD_NOT_EMIT_2"))
            testScheduler.advanceTimeBy(100L)
            expectNoEvents()
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("shouldEmit1"))
            testScheduler.advanceTimeBy(1_001L)
            sut.onNewAction(GenerateQRCodeUIAction.UpdateText("shouldEmit2"))
            assertEquals("shouldEmit1", awaitItem().content.inputText)
            assertEquals("shouldEmit2", awaitItem().content.inputText)
        }
    }
}