package com.pedroid.qrcodecompose.androidapp.features.history.domain

import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getAllHistory(): Flow<List<HistoryEntry>>

    suspend fun getSingleHistory(uid: Long): HistoryEntry

    suspend fun addHistoryEntry(entry: HistoryEntry): Long
}
