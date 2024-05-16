package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SettingsMainViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val settingsFlow = MutableSharedFlow<FullSettings>()
    private val settingsRepository =
        mockk<SettingsRepository>(relaxed = true) {
            every { getFullSettings() } returns settingsFlow.asSharedFlow()
        }
    private val languageManager =
        mockk<LanguageManager>(relaxed = true) {
            every { getAppLanguage() } returns defaultLanguage
            every { getAppLanguageFlow() } returns flowOf(defaultLanguage)
        }

    private lateinit var sut: SettingsMainViewModel

    @Before
    fun setUp() {
        sut =
            SettingsMainViewModel(
                settingsRepository = settingsRepository,
                settingsMainContentUIBuilder = SettingsMainContentUIBuilder(),
                languageManager = languageManager,
                logger = mockk<Logger>(relaxed = true),
            )
    }

    @Test
    fun `initial state has Idle content`() =
        runTest {
            assertEquals(SettingsMainUIState.Idle, sut.uiState.value)
        }

    @Test
    fun `given repository returns settings, state is updated with content`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state

                settingsFlow.emit(FullSettings())

                val result = awaitItem()
                assertTrue(result is SettingsMainUIState.Content)
                assertEquals(4, (result as SettingsMainUIState.Content).sections.size)
                assertEquals(8, result.sections.sumOf { it.items.size })
            }
        }

    @Test
    fun `given change language action, app language is updated`() =
        runTest {
            sut.onNewAction(SettingsMainUIAction.ChangeAppLanguage(AppLanguage.ENGLISH))

            coVerify {
                languageManager.setAppLanguage(AppLanguage.ENGLISH)
            }
        }

    @Test
    fun `given open url change action, settings open url is updated via repo`() =
        runTest {
            sut.onNewAction(SettingsMainUIAction.ChangeOpenUrlMode(OpenUrlPreferences.IN_CUSTOM_TAB))

            coVerify {
                settingsRepository.setOpenUrlPreferences(OpenUrlPreferences.IN_CUSTOM_TAB)
            }
        }

    @Test
    fun `given scan save history change action, scan settings save history is updated via repo`() =
        runTest {
            sut.onNewAction(SettingsMainUIAction.ChangeScanHistorySave(HistorySavePreferences.NEVER_SAVE))

            coVerify {
                settingsRepository.setScanHistorySavePreferences(HistorySavePreferences.NEVER_SAVE)
            }
        }

    @Test
    fun `given scan vibrate action change, settings scan haptic feedback is updated via repo`() =
        runTest {
            settingsFlow.emit(FullSettings(scan = ScanSettings(hapticFeedback = false)))

            sut.onNewAction(SettingsMainUIAction.ToggleScanHapticFeedback)

            coVerify {
                settingsRepository.toggleScanHapticFeedback()
            }
        }

    @Test
    fun `given generate save history change action, generate settings save history is updated via repo`() =
        runTest {
            sut.onNewAction(SettingsMainUIAction.ChangeGenerateHistorySave(HistorySavePreferences.UPON_USER_ACTION))

            coVerify {
                settingsRepository.setGenerateHistorySavePreferences(HistorySavePreferences.UPON_USER_ACTION)
            }
        }

    companion object {
        val defaultLanguage = AppLanguage.SAME_AS_SYSTEM
    }
}
