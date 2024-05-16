package com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile

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

class ScanQRCodeFromFileViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val settingsMutableFlow = MutableStateFlow(FullSettings())
    private val settingsRepository =
        mockk<SettingsReadOnlyRepository> {
            every { getFullSettings() } returns settingsMutableFlow.asStateFlow()
        }

    private lateinit var sut: ScanQRCodeFromFileViewModel

    @Before
    fun setUp() {
        sut =
            ScanQRCodeFromFileViewModel(
                settingsRepository = settingsRepository,
                logger = mockk(relaxed = true),
            )
    }

    @Test
    fun `initial UI State is Init`() {
        assertEquals(QRCodeFromFileUIState.Init, sut.uiState.value)
    }

    @Test
    fun `given qr action StartFileSelection, state is changed to Loading`() {
        sut.onNewAction(QRCodeFromFileUIAction.StartFileSelection)

        assertEquals(QRCodeFromFileUIState.Loading, sut.uiState.value)
    }

    @Test
    fun `given qr result action is with Idle, ui state does not change`() {
        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Idle))

        assertEquals(QRCodeFromFileUIState.Init, sut.uiState.value)
    }

    @Test
    fun `given qr result action is with Idle after init scan, ui state does not change`() {
        sut.onNewAction(QRCodeFromFileUIAction.StartFileSelection)

        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Idle))

        assertEquals(QRCodeFromFileUIState.Loading, sut.uiState.value)
    }

    @Test
    fun `given qr result action is with Cancelled, ui state is set to Cancelled`() {
        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Cancelled))

        assertEquals(QRCodeFromFileUIState.Cancelled, sut.uiState.value)
    }

    @Test
    fun `given qr result action is with Invalid, ui state is set to error`() {
        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Invalid))

        assertEquals(QRCodeFromFileUIState.Error, sut.uiState.value)
    }

    @Test
    fun `given qr result action is with UnrecoverableError, ui state is set to error`() {
        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.UnrecoverableError))

        assertEquals(QRCodeFromFileUIState.Error, sut.uiState.value)
    }

    @Test
    fun `given qr result action is with Scanned, ui state is set to Success`() {
        val qrCodeData = "qr_code_data"
        val format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13

        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Scanned(qrCodeData, format)))

        assertEquals(QRCodeFromFileUIState.Success(qrCodeData, format), sut.uiState.value)
    }

    @Test
    fun `given qr result action is with Scanned and haptic feedback settings, ui state is set to Success with vibrate=true`() =
        runTest {
            val qrCodeData = "qr_code_data"
            val format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13
            settingsMutableFlow.emit(FullSettings(scan = ScanSettings(hapticFeedback = true)))

            sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Scanned(qrCodeData, format)))

            assertEquals(QRCodeFromFileUIState.Success(qrCodeData, format, vibrate = true), sut.uiState.value)
        }

    @Test
    fun `given qr result action is Cancelled after error, ui state is not updated`() {
        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Invalid))

        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Cancelled))

        assertEquals(QRCodeFromFileUIState.Error, sut.uiState.value)
    }
}
