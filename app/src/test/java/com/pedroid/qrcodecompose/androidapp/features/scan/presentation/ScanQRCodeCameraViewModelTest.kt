package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsReadOnlyRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScanQRCodeCameraViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val settingsMutableFlow = MutableStateFlow(FullSettings())
    private val settingsRepository =
        mockk<SettingsReadOnlyRepository> {
            every { getFullSettings() } returns settingsMutableFlow.asStateFlow()
        }

    private lateinit var sut: ScanQRCodeCameraViewModel

    @Before
    fun setUp() {
        sut = ScanQRCodeCameraViewModel(settingsRepository)
    }

    @Test
    fun `initial uiState is Idle`() {
        assertEquals(QRCodeCameraUIState.Idle, sut.uiState.value)
    }

    @Test
    fun `given action ResultUpdate has success, ui state is updated`() {
        sut.onNewAction(QRCodeCameraUIAction.ResultUpdate(QRCodeScanResult.Scanned("someQRCode", QRCodeComposeXFormat.QR_CODE)))

        assertEquals(QRCodeCameraUIState.ScanComplete("someQRCode", QRCodeComposeXFormat.QR_CODE), sut.uiState.value)
    }

    @Test
    fun `given action ResultUpdate has success with hapticFeedback settings, ui state is updated with vibrate=true`() =
        runTest {
            settingsMutableFlow.emit(FullSettings(scan = ScanSettings(hapticFeedback = true)))

            sut.onNewAction(QRCodeCameraUIAction.ResultUpdate(QRCodeScanResult.Scanned("someQRCode", QRCodeComposeXFormat.QR_CODE)))

            assertEquals(QRCodeCameraUIState.ScanComplete("someQRCode", QRCodeComposeXFormat.QR_CODE, vibrate = true), sut.uiState.value)
        }

    @Test
    fun `given action ResultUpdate does not have success, ui state is not updated`() {
        sut.onNewAction(QRCodeCameraUIAction.ResultUpdate(QRCodeScanResult.Invalid))

        assertEquals(QRCodeCameraUIState.Idle, sut.uiState.value)
    }
}
