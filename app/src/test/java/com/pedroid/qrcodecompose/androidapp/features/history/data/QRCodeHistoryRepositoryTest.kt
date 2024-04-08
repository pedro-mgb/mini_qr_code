package com.pedroid.qrcodecompose.androidapp.features.history.data

import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDBEntity
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDao
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.ID_NEW_ENTRY
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class QRCodeHistoryRepositoryTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private val qrCodeHistoryDao = mockk<QRCodeHistoryDao>(relaxed = true)

    private lateinit var sut: QRCodeHistoryRepository

    @Before
    fun setUp() {
        sut = QRCodeHistoryRepository(qrCodeHistoryDao)
    }

    @Test
    fun `repository returns dao list mapped to HistoryEntry`() =
        runTest {
            every { qrCodeHistoryDao.getAll() } returns flowOf(historyDBEntity)

            val result = sut.getAllHistory()

            assertEquals(result.first(), expectedHistoryEntryList)
            verify { qrCodeHistoryDao.getAll() }
        }

    @Test
    fun `repository gets single dao item mapped to HistoryEntry`() =
        runTest {
            coEvery { qrCodeHistoryDao.getByUid(2L) } returns historyDBEntity.first { it.uid == 2L }

            val result = sut.getSingleHistory(2L)

            assertEquals(result, expectedHistoryEntryList.first { it.uid == 2L })
            coVerify { qrCodeHistoryDao.getByUid(2L) }
        }

    @Test
    fun `repository adds scan camera HistoryEntry by mapping it to QRCodeHistoryDBEntity and sending to dao`() =
        runTest {
            coEvery { qrCodeHistoryDao.insert(any()) } returns 4L
            val input = expectedHistoryEntryList[0]
            val expectedEntity = historyDBEntity[0].copy(uid = ID_NEW_ENTRY)

            val result = sut.addHistoryEntry(input)

            assertEquals(result, 4L)
            coVerify { qrCodeHistoryDao.insert(expectedEntity) }
        }

    @Test
    fun `repository adds scan image HistoryEntry by mapping it to QRCodeHistoryDBEntity and sending to dao`() =
        runTest {
            coEvery { qrCodeHistoryDao.insert(any()) } returns 5L
            val input = expectedHistoryEntryList[1]
            val expectedEntity = historyDBEntity[1].copy(uid = ID_NEW_ENTRY)

            val result = sut.addHistoryEntry(input)

            assertEquals(result, 5L)
            coVerify { qrCodeHistoryDao.insert(expectedEntity) }
        }

    @Test
    fun `repository adds generate HistoryEntry by mapping it to QRCodeHistoryDBEntity and sending to dao`() =
        runTest {
            coEvery { qrCodeHistoryDao.insert(any()) } returns 6L
            val input = expectedHistoryEntryList[2]
            val expectedEntity = historyDBEntity[2].copy(uid = ID_NEW_ENTRY)

            val result = sut.addHistoryEntry(input)

            assertEquals(result, 6L)
            coVerify { qrCodeHistoryDao.insert(expectedEntity) }
        }

    companion object {
        private val historyDBEntity: List<QRCodeHistoryDBEntity> =
            listOf(
                QRCodeHistoryDBEntity(
                    uid = 1L,
                    type = HistoryType.SCAN_CAMERA,
                    value = "qr code scanned from camera",
                    timeStamp = Instant.ofEpochMilli(10000L),
                    format = QRCodeComposeXFormat.QR_CODE,
                ),
                QRCodeHistoryDBEntity(
                    uid = 2L,
                    type = HistoryType.SCAN_IMAGE,
                    value = "aztec scanned from image",
                    timeStamp = Instant.ofEpochMilli(20000L),
                    format = QRCodeComposeXFormat.AZTEC,
                ),
                QRCodeHistoryDBEntity(
                    uid = 3L,
                    type = HistoryType.GENERATE,
                    value = "1234567",
                    timeStamp = Instant.ofEpochMilli(30000L),
                    format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                ),
            )

        private val expectedHistoryEntryList: List<HistoryEntry> =
            listOf(
                HistoryEntry.ScanEntry(
                    uid = 1L,
                    value = "qr code scanned from camera",
                    creationDate = Instant.ofEpochMilli(10000L),
                    format = QRCodeComposeXFormat.QR_CODE,
                    fromImage = false,
                ),
                HistoryEntry.ScanEntry(
                    uid = 2L,
                    value = "aztec scanned from image",
                    creationDate = Instant.ofEpochMilli(20000L),
                    format = QRCodeComposeXFormat.AZTEC,
                    fromImage = true,
                ),
                HistoryEntry.GenerateEntry(
                    uid = 3L,
                    value = "1234567",
                    creationDate = Instant.ofEpochMilli(30000L),
                    format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                ),
            )
    }
}
