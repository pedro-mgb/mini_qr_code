package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.core.test.assertContains
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.Locale

class HistoryListViewModelTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val historyFlow = MutableSharedFlow<List<HistoryEntry>>()
    private val historyRepository =
        mockk<HistoryRepository> {
            every { getAllHistory() } returns historyFlow.asSharedFlow()
            coEvery { deleteHistoryEntries(any()) } just runs
        }

    private lateinit var sut: HistoryListViewModel

    @Before
    fun setUp() {
        // setting default locale so that translation texts don't break if running in different region
        Locale.setDefault(Locale.US)
        sut =
            HistoryListViewModel(
                historyRepository,
                mockk<Logger>(relaxed = true),
            )
    }

    @Test
    fun `initial state has Idle content`() =
        runTest {
            assertEquals(HistoryListUIState(HistoryListContentState.Idle), sut.uiState.value)
        }

    @Test
    fun `given repository returns empty list, UI state updated with empty content state`() =
        runTest {
            sut.uiState.test {
                awaitItem() // initial state

                historyFlow.emit(emptyList())

                assertEquals(HistoryListUIState(HistoryListContentState.Empty), awaitItem())
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

                val newResultList = (awaitItem().content as HistoryListContentState.DataList).list

                assertTrue(newResultList.size == 3)
                val firstDataItem = newResultList[1] as HistoryListItem.Data
                assertEquals(1L, firstDataItem.uid)
                assertEquals("qr code scanned from camera", firstDataItem.value)
                assertEquals(HistoryTypeUI.SCAN_CAMERA, firstDataItem.typeUI)
                assertEquals(QRCodeComposeXFormat.QR_CODE.titleStringId, firstDataItem.formatStringId)
                val secondDataItem = newResultList[2] as HistoryListItem.Data
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

                val newResultList = (awaitItem().content as HistoryListContentState.DataList).list

                assertTrue(newResultList.size == 3)
                val firstDataItem = newResultList[1] as HistoryListItem.Data
                assertEquals(3L, firstDataItem.uid)
                assertEquals("data matrix scanned yesterday", firstDataItem.value)
                assertEquals(HistoryTypeUI.SCAN_FILE, firstDataItem.typeUI)
                assertEquals(QRCodeComposeXFormat.DATA_MATRIX.titleStringId, firstDataItem.formatStringId)
                val secondDataItem = newResultList[2] as HistoryListItem.Data
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
                val currentResultList = (awaitItem().content as HistoryListContentState.DataList).list
                assertTrue(currentResultList.size == 4)

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
                val newResultList = (awaitItem().content as HistoryListContentState.DataList).list
                assertTrue(newResultList.size == 6)
                assertEquals("Monday, 8 Apr 2024", (newResultList[0] as HistoryListItem.SectionHeader).text)
                assertEquals("Saturday, 16 Mar 2024", (newResultList[2] as HistoryListItem.SectionHeader).text)
                assertEquals("Wednesday, 4 Oct 2023", (newResultList[4] as HistoryListItem.SectionHeader).text)
                assertContains((newResultList[1] as HistoryListItem.Data).displayDate, "08-04-2024")
                assertContains((newResultList[3] as HistoryListItem.Data).displayDate, "16-03-2024")
                assertContains((newResultList[5] as HistoryListItem.Data).displayDate, "04-10-2023")
            }
        }

    @Test
    fun `given deleted from details action, temporary message updated as info snackbar`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(emptyList())
                skipItems(2) // idle + content state with list

                sut.onNewAction(HistoryListUIAction.DeletedFromDetails(1L))

                assertEquals(TemporaryMessageType.INFO_SNACKBAR, awaitItem().temporaryMessage?.type)
            }
        }

    @Test
    fun `given deleted success message shown, temporary message updated to null`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(emptyList())
                sut.onNewAction(HistoryListUIAction.DeletedFromDetails(1L))
                skipItems(3) // idle + content state with list + snackbar

                sut.onNewAction(HistoryListUIAction.TmpMessageShown)

                assertNull(awaitItem().temporaryMessage)
            }
        }

    @Test
    fun `given item selection action, history list state is updated with select mode and newly selected items`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(fixedHistoryListItems)
                skipItems(2) // idle + content state

                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(1L))
                assertEquals(HistorySelectionMode.Selection(1), awaitItem().selectionMode)
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(4L))

                val result = awaitItem()
                assertEquals(HistorySelectionMode.Selection(2), result.selectionMode)
                val historySelectedResult: List<Pair<Long, Boolean>> =
                    (result.content as? HistoryListContentState.DataList)?.list
                        ?.filterIsInstance<HistoryListItem.Data>()
                        ?.map {
                            Pair(it.uid, it.selected)
                        } ?: emptyList()
                val expectedSelectedItems =
                    fixedHistoryListItems.map {
                        Pair(it.uid, it.uid in listOf(1L, 4L))
                    }
                assertEquals(
                    expectedSelectedItems,
                    historySelectedResult,
                )
            }
        }

    @Test
    fun `given item duplicate selection toggle action, history list state is updated with that item unselected`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(fixedHistoryListItems)
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(1L))
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(4L))
                skipItems(4) // idle + content state + 2 selections

                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(4L))

                val result = awaitItem()
                assertEquals(HistorySelectionMode.Selection(1), result.selectionMode)
                val historySelectedResult: HistoryListItem.Data? =
                    (result.content as? HistoryListContentState.DataList)?.list
                        ?.filterIsInstance<HistoryListItem.Data>()
                        ?.firstOrNull {
                            it.uid == 4L
                        }
                assertEquals(false, historySelectedResult?.selected)
            }
        }

    @Test
    fun `given select all action, history list state is updated with all items selected`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(fixedHistoryListItems)
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(2L))
                skipItems(3) // idle + content state + 1 item selected

                sut.onNewAction(HistoryListUIAction.SelectAll)

                val result = awaitItem()
                assertEquals(HistorySelectionMode.Selection(fixedHistoryListItems.size), result.selectionMode)
                val historySelectedResult =
                    (result.content as? HistoryListContentState.DataList)?.list
                        ?.filterIsInstance<HistoryListItem.Data>() ?: emptyList()
                assertTrue(historySelectedResult.all { it.selected })
                assertTrue(result.allSelected)
                expectNoEvents()
            }
        }

    @Test
    fun `given cancel action after selection, history list state is updated with no items selected and select mode off`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(fixedHistoryListItems)
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(2L))
                sut.onNewAction(HistoryListUIAction.SelectAll)
                skipItems(4) // idle + content state + 1 item selected + select all

                sut.onNewAction(HistoryListUIAction.CancelSelection)

                val result = awaitItem()
                assertEquals(HistorySelectionMode.None, result.selectionMode)
                val historySelectedResult =
                    (result.content as? HistoryListContentState.DataList)?.list
                        ?.filterIsInstance<HistoryListItem.Data>() ?: emptyList()
                assertTrue(historySelectedResult.all { !it.selected })
            }
        }

    @Test
    fun `given delete action after selection, history delete is called with selected items and selection is removed`() =
        runTest {
            sut.uiState.test {
                historyFlow.emit(fixedHistoryListItems)
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(4L))
                sut.onNewAction(HistoryListUIAction.SelectedItemToggle(1L))
                skipItems(4) // idle + content state + 2 selections

                sut.onNewAction(HistoryListUIAction.DeleteSelected)

                coVerify {
                    historyRepository.deleteHistoryEntries(listOf(4L, 1L))
                }
                val result = awaitItem()
                assertEquals(HistorySelectionMode.None, result.selectionMode)
                assertEquals(TemporaryMessageType.INFO_SNACKBAR, result.temporaryMessage?.type)
                expectNoEvents()
            }
        }

    companion object {
        private val fixedHistoryListItems =
            listOf(
                HistoryEntry.Scan(
                    uid = 1L,
                    value = "qr code scanned from camera",
                    creationDate = Instant.now(),
                    format = QRCodeComposeXFormat.QR_CODE,
                    fromImageFile = false,
                ),
                HistoryEntry.Generate(
                    uid = 2L,
                    value = "1234567",
                    creationDate = Instant.now().minusMillis(1_000L),
                    format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                ),
                HistoryEntry.Scan(
                    uid = 3L,
                    value = "qr code scanned from camera",
                    creationDate = Instant.now().minusMillis(10_000_000L),
                    format = QRCodeComposeXFormat.QR_CODE,
                    fromImageFile = false,
                ),
                HistoryEntry.Scan(
                    uid = 4L,
                    value = "aztec scanned from image",
                    creationDate = Instant.now().minusMillis(12_000_000L),
                    format = QRCodeComposeXFormat.AZTEC,
                    fromImageFile = true,
                ),
            )
    }
}
