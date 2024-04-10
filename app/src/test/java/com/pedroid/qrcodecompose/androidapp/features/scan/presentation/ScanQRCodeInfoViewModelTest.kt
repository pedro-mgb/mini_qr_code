package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScanSource
import com.pedroid.qrcodecompose.androidapp.features.scan.data.ScannedCode
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ScanQRCodeInfoViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private lateinit var sut: ScanQRCodeInfoViewModel

    private val historyRepository =
        mockk<HistoryRepository> {
            coEvery { addHistoryEntry(any()) } returns 1L
        }

    @Before
    fun setUp() {
        sut =
            ScanQRCodeInfoViewModel(
                historyRepository = historyRepository,
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

    @Test
    fun `given action success on scanned qr code, the qr code is saved to history`() =
        runTest {
            sut.onNewAction(QRCodeInfoUIAction.CodeReceived(ScannedCode("qr code", QRCodeComposeXFormat.QR_CODE, ScanSource.IMAGE_FILE)))

            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            val entrySlot = slot<HistoryEntry.Scan>()

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(capture(entrySlot))
            }
            val capturedEntry = entrySlot.captured
            assertEquals(capturedEntry.value, "qr code")
            assertEquals(capturedEntry.format, QRCodeComposeXFormat.QR_CODE)
            assertEquals(capturedEntry.fromImageFile, true)
        }

    @Test
    fun `given 2 action success on same code, only saved to history once`() =
        runTest {
            sut.onNewAction(QRCodeInfoUIAction.CodeReceived(ScannedCode("qr code", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA)))

            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given error on action, not saved to history`() =
        runTest {
            sut.onNewAction(QRCodeInfoUIAction.CodeReceived(ScannedCode("qr code", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA)))

            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.ERROR_FILE)))

            coVerify(inverse = true) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given action without scanned code, not saved to history`() =
        runTest {
            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.SaveToFile(ActionStatus.SUCCESS)))

            coVerify(inverse = true) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given 2 actions with same scanned code, saved to history only once`() =
        runTest {
            sut.onNewAction(QRCodeInfoUIAction.CodeReceived(ScannedCode("qr code", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA)))

            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))

            coVerify(exactly = 1) {
                historyRepository.addHistoryEntry(any())
            }
        }

    @Test
    fun `given 2 actions with different scanned codes, saved to history twice`() =
        runTest {
            sut.onNewAction(QRCodeInfoUIAction.CodeReceived(ScannedCode("qr code", QRCodeComposeXFormat.QR_CODE, ScanSource.CAMERA)))

            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.Copy(ActionStatus.SUCCESS)))
            sut.onNewAction(QRCodeInfoUIAction.CodeReceived(ScannedCode("123456", QRCodeComposeXFormat.BARCODE_ITF, ScanSource.IMAGE_FILE)))
            sut.onNewAction(QRCodeInfoUIAction.QRActionComplete(QRAppActions.ShareApp(ActionStatus.SUCCESS)))
            val list = mutableListOf<HistoryEntry.Scan>()

            coVerify(exactly = 2) {
                historyRepository.addHistoryEntry(capture(list))
            }
            assertEquals(list.map { it.value }, listOf("qr code", "123456"))
            assertEquals(list.map { it.format }, listOf(QRCodeComposeXFormat.QR_CODE, QRCodeComposeXFormat.BARCODE_ITF))
            assertEquals(list.map { it.fromImageFile }, listOf(false, true))
        }

    private fun TemporaryMessageData?.assertHasError() {
        assertTrue(this != null)
        assertTrue(this!!.text.isNotBlank())
        assertEquals(TemporaryMessageType.ERROR_SNACKBAR, this.type)
    }
}
