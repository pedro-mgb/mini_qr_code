package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import com.pedroid.qrcodecompose.androidapp.core.presentation.AppResponseStatus
import com.pedroid.qrcodecompose.androidapp.core.presentation.ExternalAppStartResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
        assertEquals(sut.uiState.value.content, QRCodeInfoContentUIState.Initial)
        assertEquals(sut.uiState.value.errorMessageKey, "")
    }

    @Test
    fun `given action without qr code is received, initial UI state remains`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = null))

        assertEquals(sut.uiState.value.content, QRCodeInfoContentUIState.Initial)
    }

    @Test
    fun `given action with empty qr code is received, initial UI state remains`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = ""))

        assertEquals(QRCodeInfoContentUIState.Initial, sut.uiState.value.content)
    }

    @Test
    fun `given action without actual qr code is received, CodeScanned state is emitted`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = "some_qr_code"))

        assertEquals(
            QRCodeInfoContentUIState.CodeScanned("some_qr_code"),
            sut.uiState.value.content,
        )
    }

    @Test
    fun `given action for external app was opened successfully, no error message is emitted`() {
        sut.onNewAction(
            QRCodeInfoUIAction.AppStarted(
                ExternalAppStartResponse.OpenApp(AppResponseStatus.SUCCESS),
            ),
        )

        assertEquals("", sut.uiState.value.errorMessageKey)
    }

    @Test
    fun `given action for external app has error, error message is emitted in state`() {
        sut.onNewAction(
            QRCodeInfoUIAction.AppStarted(
                ExternalAppStartResponse.ShareApp(AppResponseStatus.ERROR_NO_APP),
            ),
        )

        assertTrue(sut.uiState.value.errorMessageKey.isNotBlank())
    }

    @Test
    fun `given error shown action is sent after external app has error, error message is emitted in state`() {
        sut.onNewAction(
            QRCodeInfoUIAction.AppStarted(
                ExternalAppStartResponse.ShareApp(AppResponseStatus.ERROR_NO_APP),
            ),
        )

        sut.onNewAction(QRCodeInfoUIAction.ErrorShown)

        assertEquals("", sut.uiState.value.errorMessageKey)
    }
}
