package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test


class ScanQRCodeInfoViewModelTest {

    private lateinit var sut: ScanQRCodeInfoViewModel

    @Before
    fun setUp() {
        sut = ScanQRCodeInfoViewModel()
    }

    @Test
    fun `given sut is created, state is initial`() {
        assertEquals(sut.uiState.value, QRCodeInfoUIState.Initial)
    }

    @Test
    fun `given action without qr code is received, initial UI state remains`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = null))

        assertEquals(sut.uiState.value, QRCodeInfoUIState.Initial)
    }

    @Test
    fun `given action with empty qr code is received, initial UI state remains`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = ""))

        assertEquals(sut.uiState.value, QRCodeInfoUIState.Initial)
    }

    @Test
    fun `given action without actual qr code is received, CodeScanned state is emitted`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = "some_qr_code"))

        assertEquals(sut.uiState.value, QRCodeInfoUIState.CodeScanned("some_qr_code"))
    }
}