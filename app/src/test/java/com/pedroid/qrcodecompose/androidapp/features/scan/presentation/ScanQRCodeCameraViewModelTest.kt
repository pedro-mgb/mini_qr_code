package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ScanQRCodeCameraViewModelTest {
    private lateinit var sut: ScanQRCodeCameraViewModel

    @Before
    fun setUp() {
        sut = ScanQRCodeCameraViewModel()
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
    fun `given action ResultUpdate does not have success, ui state is not updated`() {
        sut.onNewAction(QRCodeCameraUIAction.ResultUpdate(QRCodeScanResult.Invalid))

        assertEquals(QRCodeCameraUIState.Idle, sut.uiState.value)
    }
}
