package com.pedroid.qrcodecompose.androidapp.features.history.data

import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDBEntity
import com.pedroid.qrcodecompose.androidapp.features.history.data.db.QRCodeHistoryDao
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QRCodeHistoryRepository
    @Inject
    constructor(
        private val dao: QRCodeHistoryDao,
    ) : HistoryRepository {
        override fun getAllHistory(): Flow<List<HistoryEntry>> {
            return dao.getAll().map { entityList ->
                entityList.map { entity ->
                    entity.toHistoryEntry()
                }
            }
        }

        override fun getSingleHistory(uid: Long): Flow<HistoryEntry?> {
            return dao.getByUid(uid).map {
                it?.toHistoryEntry()
            }
        }

        override suspend fun addHistoryEntry(entry: HistoryEntry): Long {
            return dao.insert(entry.toDBEntity())
        }

        override suspend fun deleteHistoryEntries(idList: List<Long>) {
            dao.deleteAllMatchingIds(idList)
        }

        private fun QRCodeHistoryDBEntity.toHistoryEntry(): HistoryEntry =
            when (type) {
                HistoryType.GENERATE -> {
                    HistoryEntry.Generate(
                        uid = uid,
                        value = value,
                        creationDate = timeStamp,
                        format = format,
                    )
                }

                else -> {
                    HistoryEntry.Scan(
                        uid = uid,
                        value = value,
                        creationDate = timeStamp,
                        format = format,
                        fromImageFile = type == HistoryType.SCAN_IMAGE_FILE,
                    )
                }
            }

        private fun HistoryEntry.toDBEntity(): QRCodeHistoryDBEntity =
            when (this) {
                is HistoryEntry.Scan -> {
                    QRCodeHistoryDBEntity(
                        type =
                            if (!this.fromImageFile) {
                                HistoryType.SCAN_CAMERA
                            } else {
                                HistoryType.SCAN_IMAGE_FILE
                            },
                        value = this.value,
                        timeStamp = this.creationDate,
                        format = this.format,
                    )
                }
                is HistoryEntry.Generate -> {
                    QRCodeHistoryDBEntity(
                        type = HistoryType.GENERATE,
                        value = this.value,
                        timeStamp = this.creationDate,
                        format = this.format,
                    )
                }
            }
    }
