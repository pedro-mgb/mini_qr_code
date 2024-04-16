package com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryTypeUI
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale

internal class HistoryDetailViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val detailFlow = MutableSharedFlow<HistoryEntry?>()
    private val historyRepository =
        mockk<HistoryRepository> {
            every { getSingleHistory(any()) } returns detailFlow.asSharedFlow()
        }

    private lateinit var sut: HistoryDetailViewModel

    @Before
    fun setUp() {
        sut =
            HistoryDetailViewModel(
                entryUid = UID,
                historyRepository = historyRepository,
                logger = mockk(relaxed = true),
            )
    }

    @Test
    fun `initial state is Idle`() =
        runTest {
            assertEquals(HistoryDetailUIState.Idle, sut.uiState.value)
        }

    @Test
    fun `given repository does not find entry, Deleted state is emitted`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state

                detailFlow.emit(null)

                assertEquals(HistoryDetailUIState.Deleted, awaitItem())
            }
        }

    @Test
    fun `given repository finds entry and it is scan, Content state is emitted with scan history details`() =
        runTest {
            Locale.setDefault(Locale.UK)
            val expected =
                HistoryDetail(
                    uid = UID,
                    value = "scanned qr code from file",
                    format = QRCodeComposeXFormat.QR_CODE,
                    displayDate = "Sunday, 17 Mar 2024 - 15:00:00",
                    typeUI = HistoryTypeUI.SCAN_FILE,
                )
            sut.uiState.test {
                awaitItem() // initial state

                detailFlow.emit(historyEntryScan)

                assertEquals(HistoryDetailUIState.Content(expected), awaitItem())
            }
        }

    @Test
    fun `given repository finds entry and it is generate, Content state is emitted with generate history details`() =
        runTest {
            Locale.setDefault(Locale.US)
            val expected =
                HistoryDetail(
                    uid = UID,
                    value = "123456",
                    format = QRCodeComposeXFormat.BARCODE_ITF,
                    displayDate = "Sunday, 17 Mar 2024 - 3:00:00 PM",
                    typeUI = HistoryTypeUI.GENERATE,
                )
            sut.uiState.test {
                awaitItem() // initial state

                detailFlow.emit(historyEntryGenerate)

                assertEquals(HistoryDetailUIState.Content(expected), awaitItem())
            }
        }

    companion object {
        private const val UID: Long = 1L
        private val date = LocalDateTime.of(2024, 3, 17, 15, 0, 0)

        private val historyEntryScan =
            HistoryEntry.Scan(
                uid = UID,
                value = "scanned qr code from file",
                creationDate = date.toInstant(ZoneOffset.ofHours(0)),
                format = QRCodeComposeXFormat.QR_CODE,
                fromImageFile = true,
            )

        private val historyEntryGenerate =
            HistoryEntry.Generate(
                uid = UID,
                value = "123456",
                creationDate = date.toInstant(ZoneOffset.ofHours(0)),
                format = QRCodeComposeXFormat.BARCODE_ITF,
            )
    }
}
