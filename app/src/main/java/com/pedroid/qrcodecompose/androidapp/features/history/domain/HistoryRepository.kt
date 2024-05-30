package com.pedroid.qrcodecompose.androidapp.features.history.domain

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getAllHistory(): Flow<List<HistoryEntry>>

    fun getSingleHistory(uid: Long): Flow<HistoryEntry?>

    suspend fun addHistoryEntry(entry: HistoryEntry): Long

    suspend fun deleteHistoryEntries(idList: List<Long>)
}
