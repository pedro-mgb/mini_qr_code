package com.pedroid.qrcodecompose.androidapp.bridge.data.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.history.data.HistoryType
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDBEntity
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDao
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.Instant

@RunWith(AndroidJUnit4::class)
internal class QRCodeHistoryDaoTest {
    @get:Rule
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    private lateinit var db: QRCodeDatabase
    private lateinit var sut: QRCodeHistoryDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db =
            Room.inMemoryDatabaseBuilder(
                context,
                QRCodeDatabase::class.java,
            ).build()
        sut = db.qrCodeHistoryDao()
    }

    @After
    fun tearDown() {
        db.clearAllTables()
        db.close()
    }

    @Test
    fun when_database_empty_initially_getAll_returns_emptyList() =
        runTest {
            Assert.assertTrue(sut.getAll().first().isEmpty())
        }

    @Test
    fun when_database_empty_initially_getByUid_returns_null() =
        runTest {
            Assert.assertEquals(sut.getByUid(1L), null)
        }

    @Test
    fun when_inserting_getAll_flow_updates_with_new_items_with_newest_first() =
        runTest {
            sut.getAll().test {
                awaitItem() // empty list

                val id1 = sut.insert(testDBEntity1)
                Assert.assertEquals(awaitItem(), listOf(testDBEntity1.copy(uid = id1)))
                val id2 = sut.insert(testDBEntity2)
                Assert.assertEquals(
                    awaitItem(),
                    listOf(testDBEntity2.copy(uid = id2), testDBEntity1.copy(uid = id1)),
                )
                val id3 = sut.insert(testDBEntity3)
                Assert.assertEquals(
                    awaitItem(),
                    listOf(
                        testDBEntity3.copy(uid = id3),
                        testDBEntity2.copy(uid = id2),
                        testDBEntity1.copy(uid = id1),
                    ),
                )
                expectNoEvents()
            }
        }

    @Test
    fun when_inserting_getByUid_returns_item() =
        runTest {
            val id = sut.insert(testDBEntity3)

            Assert.assertEquals(sut.getByUid(id), testDBEntity3.copy(uid = id))
        }

    @Test
    fun when_deleting_getAll_flow_updates_without_removed_item_in_list() =
        runTest {
            val id1 = sut.insert(testDBEntity1)
            val idToRemove = sut.insert(testDBEntity2)
            val id3 = sut.insert(testDBEntity3)
            sut.getAll().test {
                Assert.assertTrue(awaitItem().size == 3)

                sut.deleteById(idToRemove)
                val resultList = awaitItem()

                Assert.assertTrue(resultList.size == 2)
                Assert.assertEquals(
                    resultList,
                    listOf(testDBEntity3.copy(uid = id3), testDBEntity1.copy(uid = id1)),
                )
                expectNoEvents()
            }
        }

    @Test
    fun when_deleting_getByUid_returns_null() =
        runTest {
            val idToRemove = sut.insert(testDBEntity1)

            sut.deleteById(idToRemove)

            Assert.assertEquals(sut.getByUid(idToRemove), null)
        }

    @Test
    fun when_deleting_getAll_flow_updates_without_removed_items() =
        runTest {
            val idToRemove1 = sut.insert(testDBEntity1)
            val idToRemove2 = sut.insert(testDBEntity2)
            val id3 = sut.insert(testDBEntity3)
            sut.getAll().test {
                Assert.assertTrue(awaitItem().size == 3)

                sut.deleteAllMatchingIds(listOf(idToRemove1, idToRemove2))
                val resultList = awaitItem()

                Assert.assertTrue(resultList.size == 1)
                Assert.assertEquals(resultList, listOf(testDBEntity3.copy(uid = id3)))
                expectNoEvents()
            }
        }

    @Test
    fun when_deleteAll_getAll_flow_updates_with_emptyList() =
        runTest {
            sut.insert(testDBEntity1)
            sut.insert(testDBEntity2)
            sut.insert(testDBEntity3)
            sut.getAll().test {
                Assert.assertTrue(awaitItem().size == 3)

                sut.deleteAll()

                Assert.assertTrue(awaitItem().isEmpty())
                expectNoEvents()
            }
        }

    companion object {
        val testDBEntity1 =
            QRCodeHistoryDBEntity(
                type = HistoryType.SCAN_CAMERA,
                value = "qr code scanned from camera",
                timeStamp = Instant.ofEpochMilli(10000L),
                format = QRCodeComposeXFormat.QR_CODE,
            )
        val testDBEntity2 =
            QRCodeHistoryDBEntity(
                type = HistoryType.SCAN_IMAGE,
                value = "BARCODE",
                timeStamp = Instant.ofEpochMilli(20000L),
                format = QRCodeComposeXFormat.BARCODE_128,
            )
        val testDBEntity3 =
            QRCodeHistoryDBEntity(
                type = HistoryType.GENERATE,
                value = "PDF417",
                timeStamp = Instant.ofEpochMilli(30000L),
                format = QRCodeComposeXFormat.PDF_417,
            )
    }
}
