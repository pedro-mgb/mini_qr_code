package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QRCodeHistoryDao {
    @Query("SELECT * FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE} ORDER BY timestamp DESC")
    fun getAll(): Flow<List<QRCodeHistoryDBEntity>>

    @Query("SELECT * FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE} WHERE uid = :uid")
    fun getByUid(uid: Long): Flow<QRCodeHistoryDBEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: QRCodeHistoryDBEntity): Long

    @Query("DELETE FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE} WHERE uid = :uid")
    suspend fun deleteById(uid: Long)

    @Query("DELETE FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE} WHERE uid in (:uidList)")
    suspend fun deleteAllMatchingIds(uidList: List<Long>)

    @Query("DELETE FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE}")
    suspend fun deleteAll()
}
