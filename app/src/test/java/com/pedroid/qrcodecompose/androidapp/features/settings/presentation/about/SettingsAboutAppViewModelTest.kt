package com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsAboutAppViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val settingsFlow = MutableStateFlow(FullSettings())
    private val settingsRepository =
        mockk<SettingsRepository>(relaxed = true) {
            every { getFullSettings() } returns settingsFlow.asStateFlow()
        }

    private lateinit var sut: SettingsAboutAppViewModel

    @Before
    fun setUp() {
        sut = SettingsAboutAppViewModel(settingsRepository)
    }

    @Test
    fun `given settings are updated, UI State is updated`() =
        runTest {
            sut.uiState.test {
                awaitItem().let { initialState ->
                    assertNull(initialState.temporaryMessage)
                    assertEquals(OpenUrlPreferences.DEFAULT, initialState.openUrlMode)
                }

                settingsFlow.emit(FullSettings(general = GeneralSettings(openUrlPreferences = OpenUrlPreferences.IN_CUSTOM_TAB)))

                assertEquals(OpenUrlPreferences.IN_CUSTOM_TAB, awaitItem().openUrlMode)
            }
        }

    @Test
    fun `given url open action is success, no updates are done to ui state`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial emission

                sut.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(QRAppActions.OpenApp(ActionStatus.SUCCESS)))

                expectNoEvents()
            }
        }

    @Test
    fun `given url open action is error, temporary message is updated as error on ui state`() =
        runTest {
            sut.uiState.test {
                awaitItem() // error emission

                sut.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(QRAppActions.OpenApp(ActionStatus.ERROR_NO_APP)))

                assertEquals(TemporaryMessageType.ERROR_SNACKBAR, awaitItem().temporaryMessage?.type)
            }
        }

    @Test
    fun `given tmp message is shown after error, temporary message is set to null on ui state`() =
        runTest {
            sut.onNewAction(SettingsAboutAppUIAction.OpenUrlAttempt(QRAppActions.OpenApp(ActionStatus.ERROR_NO_APP)))
            sut.uiState.test {
                awaitItem() // error

                sut.onNewAction(SettingsAboutAppUIAction.TmpMessageShown)

                assertNull(awaitItem().temporaryMessage)
            }
        }
}
