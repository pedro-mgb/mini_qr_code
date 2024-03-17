package com.pedroid.qrcodecompose.androidapp.features.scan.presentation.fromfile

import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScanQRCodeFromFileViewModelTest {
    private lateinit var sut: ScanQRCodeFromFileViewModel

    @Before
    fun setUp() {
        sut = ScanQRCodeFromFileViewModel(logger = mockk(relaxed = true))
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

        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Scanned(qrCodeData)))

        assertEquals(QRCodeFromFileUIState.Success(qrCodeData), sut.uiState.value)
    }

    @Test
    fun `given qr result action is Cancelled after error, ui state is not updated`() {
        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Invalid))

        sut.onNewAction(QRCodeFromFileUIAction.ScanResult(QRCodeScanResult.Cancelled))

        assertEquals(QRCodeFromFileUIState.Error, sut.uiState.value)
    }
}
