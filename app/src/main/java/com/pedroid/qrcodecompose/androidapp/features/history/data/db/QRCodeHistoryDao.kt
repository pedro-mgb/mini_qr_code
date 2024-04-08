package com.pedroid.qrcodecompose.androidapp.features.history.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QRCodeHistoryDao {
    @Query("SELECT * FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE}")
    fun getAll(): Flow<List<QRCodeHistoryDBEntity>>

    @Query("SELECT * FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE} WHERE uid = :uid")
    suspend fun getByUid(uid: Long): QRCodeHistoryDBEntity

    @Insert
    suspend fun insert(entity: QRCodeHistoryDBEntity): Long

    @Delete
    suspend fun delete(entity: QRCodeHistoryDBEntity)

    @Query("DELETE FROM ${QRCodeHistoryDBConstants.HISTORY_TABLE}")
    suspend fun deleteAll()
}
