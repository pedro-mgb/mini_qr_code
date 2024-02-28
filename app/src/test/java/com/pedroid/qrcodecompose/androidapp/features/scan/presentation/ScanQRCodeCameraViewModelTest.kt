package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import com.pedroid.qrcode_compose_x.scan.QRCodeScanResult
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
        assertEquals(sut.uiState.value, QRCodeCameraUIState.Idle)
    }

    @Test
    fun `given action ResultUpdate has success, ui state is updated`() {
        sut.onNewAction(QRCodeCameraUIAction.ResultUpdate(QRCodeScanResult.Scanned("someQRCode")))

        assertEquals(sut.uiState.value, QRCodeCameraUIState.ScanComplete("someQRCode"))
    }

    @Test
    fun `given action ResultUpdate does not have success, ui state is not updated`() {
        sut.onNewAction(QRCodeCameraUIAction.ResultUpdate(QRCodeScanResult.Invalid))

        assertEquals(sut.uiState.value, QRCodeCameraUIState.Idle)
    }
}
