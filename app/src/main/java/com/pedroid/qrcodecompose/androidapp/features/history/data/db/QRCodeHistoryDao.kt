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
    fun getByUid(uid: Int): Flow<QRCodeHistoryDBEntity>

    @Insert
    fun insert(entity: QRCodeHistoryDBEntity): Long

    @Delete
    fun delete(entity: QRCodeHistoryDBEntity)
}
