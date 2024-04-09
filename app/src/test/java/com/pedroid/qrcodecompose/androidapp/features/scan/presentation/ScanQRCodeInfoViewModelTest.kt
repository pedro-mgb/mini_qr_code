package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScanSource
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ScanQRCodeInfoViewModelTest {
    private lateinit var sut: ScanQRCodeInfoViewModel

    @Before
    fun setUp() {
        sut =
            ScanQRCodeInfoViewModel(
                logger = mockk(relaxed = true),
            )
    }

    @Test
    fun `given sut is created, state is initial`() {
        assertEquals(QRCodeInfoContentUIState.Initial, sut.uiState.value.content)
        assertEquals(null, sut.uiState.value.temporaryMessage)
    }

    @Test
    fun `given action without qr code is received, initial UI state remains`() {
        sut.onNewAction(QRCodeInfoUIAction.CodeReceived(qrCode = null))

        assertEquals(QRCodeInfoContentUIState.Initial, sut.uiState.value.content)
    }

    @Test
    fun `given action with empty qr code is received, initial UI state remains`() {
        sut.onNewAction(
            QRCodeInfoUIAction.CodeReceived(qrCode = ScannedCode("", QRCodeComposeXFormat.BARCODE_US_UPC_E, ScanSource.IMAGE_FILE)),
        )

        assertEquals(QRCodeInfoContentUIState.Initial, sut.uiState.value.content)
    }

    @Test
    fun `given action without actual qr code is received, CodeScanned state is emitted`() {
        sut.onNewAction(
            QRCodeInfoUIAction.CodeReceived(qrCode = ScannedCode("some_qr_code", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA)),
        )

        assertEquals(
            QRCodeInfoContentUIState.CodeScanned(ScannedCode("some_qr_code", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA)),
            sut.uiState.value.content,
        )
    }

    @Test
    fun `given action for external app was opened successfully, no error message is emitted`() {
        sut.onNewAction(
            QRCodeInfoUIAction.QRActionComplete(
                QRAppActions.OpenApp(ActionStatus.SUCCESS),
            ),
        )

        assertEquals(null, sut.uiState.value.temporaryMessage)
    }

    @Test
    fun `given action for external app has error, error message is emitted in state`() {
        sut.onNewAction(
            QRCodeInfoUIAction.QRActionComplete(
                QRAppActions.ShareApp(ActionStatus.ERROR_NO_APP),
            ),
        )

        sut.uiState.value.temporaryMessage.assertHasError()
    }

    @Test
    fun `given error shown action is sent after external app has error, error message is emitted in state`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                sut.onNewAction(
                    QRCodeInfoUIAction.QRActionComplete(
                        QRAppActions.ShareApp(ActionStatus.ERROR_NO_APP),
                    ),
                )
                awaitItem().temporaryMessage.assertHasError()
                sut.onNewAction(QRCodeInfoUIAction.TmpMessageShown)
                assertTrue(awaitItem().temporaryMessage == null)
            }
        }

    private fun TemporaryMessageData?.assertHasError() {
        assertTrue(this != null)
        assertTrue(this!!.text.isNotBlank())
        assertEquals(TemporaryMessageType.ERROR_SNACKBAR, this.type)
    }
}
