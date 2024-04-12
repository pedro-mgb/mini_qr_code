package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class HistoryListViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val historyFlow = MutableSharedFlow<List<HistoryEntry>>()
    private val historyRepository =
        mockk<HistoryRepository> {
            every { getAllHistory() } returns historyFlow
        }

    private lateinit var sut: HistoryListViewModel

    @Before
    fun setUp() {
        sut =
            HistoryListViewModel(
                historyRepository,
                mockk<Logger>(relaxed = true),
            )
    }

    @Test
    fun `initial state has just info header`() =
        runTest {
            assertEquals(HistoryListUIState(listOf(HistoryListItem.InfoHeader)), sut.uiState.value)
        }

    @Test
    fun `given repository returns empty list, UI state updated with empty list item`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state

                historyFlow.emit(emptyList())

                assertEquals(listOf(HistoryListItem.InfoHeader, HistoryListItem.EmptyList), awaitItem().list)
            }
        }

    @Test
    fun `given repository returns list items today, UI state updated with today section + data`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state

                val today = Instant.now()
                historyFlow.emit(
                    listOf(
                        HistoryEntry.Scan(
                            uid = 1L,
                            value = "qr code scanned from camera",
                            creationDate = today,
                            format = QRCodeComposeXFormat.QR_CODE,
                            fromImageFile = false,
                        ),
                        HistoryEntry.Generate(
                            uid = 2L,
                            value = "1234567",
                            creationDate = today.minusMillis(1_000L),
                            format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                        ),
                    ),
                )

                val newResultList = awaitItem().list

                assertTrue(newResultList.size == 4)
                assertEquals(
                    listOf(
                        HistoryListItem.InfoHeader,
                        HistoryListItem.SectionHeader("history_header_today"),
                    ),
                    newResultList.subList(0, 2),
                )
                val firstDataItem = newResultList[2] as HistoryListItem.Data
                assertEquals(1L, firstDataItem.uid)
                assertEquals("qr code scanned from camera", firstDataItem.value)
                assertEquals(HistoryTypeUI.SCAN_CAMERA, firstDataItem.typeUI)
                assertEquals(QRCodeComposeXFormat.QR_CODE.titleStringId, firstDataItem.formatStringId)
                val secondDataItem = newResultList[3] as HistoryListItem.Data
                assertEquals(2L, secondDataItem.uid)
                assertEquals("1234567", secondDataItem.value)
                assertEquals(HistoryTypeUI.GENERATE, secondDataItem.typeUI)
                assertEquals(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.titleStringId, secondDataItem.formatStringId)
            }
        }

    @Test
    fun `given repository returns list items yesterday, UI state updated with yesterday section + data`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state
                val yesterday = Instant.now().minus(1, ChronoUnit.DAYS)
                historyFlow.emit(
                    listOf(
                        HistoryEntry.Scan(
                            uid = 3L,
                            value = "data matrix scanned yesterday",
                            creationDate = yesterday,
                            format = QRCodeComposeXFormat.DATA_MATRIX,
                            fromImageFile = true,
                        ),
                        HistoryEntry.Generate(
                            uid = 4L,
                            value = "123456789012",
                            creationDate = yesterday.minusMillis(1_000L),
                            format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13,
                        ),
                    ),
                )

                val newResultList = awaitItem().list

                assertTrue(newResultList.size == 4)
                assertEquals(
                    listOf(
                        HistoryListItem.InfoHeader,
                        HistoryListItem.SectionHeader("history_header_yesterday"),
                    ),
                    newResultList.subList(0, 2),
                )
                val firstDataItem = newResultList[2] as HistoryListItem.Data
                assertEquals(3L, firstDataItem.uid)
                assertEquals("data matrix scanned yesterday", firstDataItem.value)
                assertEquals(HistoryTypeUI.SCAN_FILE, firstDataItem.typeUI)
                assertEquals(QRCodeComposeXFormat.DATA_MATRIX.titleStringId, firstDataItem.formatStringId)
                val secondDataItem = newResultList[3] as HistoryListItem.Data
                assertEquals(4L, secondDataItem.uid)
                assertEquals("123456789012", secondDataItem.value)
                assertEquals(HistoryTypeUI.GENERATE, secondDataItem.typeUI)
                assertEquals(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.titleStringId, secondDataItem.formatStringId)
            }
        }

    @Test
    fun `given repository returns list with entries in different days, returns items in different sections`() =
        runTest {
            val date1 = LocalDateTime.of(2023, 10, 4, 13, 25, 50)
            val date2 = LocalDateTime.of(2024, 3, 16, 9, 0, 0)
            val entryList: MutableList<HistoryEntry> =
                mutableListOf(
                    HistoryEntry.Scan(
                        uid = 1L,
                        value = "qr code scanned from camera",
                        creationDate = date1.toInstant(ZoneOffset.ofHours(0)),
                        format = QRCodeComposeXFormat.QR_CODE,
                        fromImageFile = false,
                    ),
                    HistoryEntry.Scan(
                        uid = 2L,
                        value = "aztec scanned from image",
                        creationDate = date2.toInstant(ZoneOffset.ofHours(0)),
                        format = QRCodeComposeXFormat.AZTEC,
                        fromImageFile = true,
                    ),
                )

            sut.uiState.test {
                awaitItem() // initial state

                historyFlow.emit(entryList.toList())
                val currentResultList = awaitItem().list
                assertTrue(currentResultList.size == 5)

                val date3 = LocalDateTime.of(2024, 4, 8, 8, 8, 8)
                entryList.add(
                    HistoryEntry.Generate(
                        uid = 3L,
                        value = "1234567",
                        creationDate = date3.toInstant(ZoneOffset.ofHours(0)),
                        format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                    ),
                )
                historyFlow.emit(entryList.toList())
                val newResultList = awaitItem().list
                assertTrue(newResultList.size == 7)
                assertEquals("Monday, 8 Apr 2024", (newResultList[1] as HistoryListItem.SectionHeader).text)
                assertEquals("Saturday, 16 Mar 2024", (newResultList[3] as HistoryListItem.SectionHeader).text)
                assertEquals("Wednesday, 4 Oct 2023", (newResultList[5] as HistoryListItem.SectionHeader).text)
                assertTrue((newResultList[2] as HistoryListItem.Data).formattedDate.contains("08-04-2024"))
                assertTrue((newResultList[4] as HistoryListItem.Data).formattedDate.contains("16-03-2024"))
                assertTrue((newResultList[6] as HistoryListItem.Data).formattedDate.contains("04-10-2023"))
            }
        }
}
