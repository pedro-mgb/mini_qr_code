package com.pedroid.qrcodecompose.androidapp.features.scan.data

import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.history.domain.ID_NEW_ENTRY
import java.time.Instant

suspend fun HistoryRepository.saveScannedCode(data: ScannedCode): Long {
    return this.addHistoryEntry(data.toHistoryEntry())
}

private fun ScannedCode.toHistoryEntry(): HistoryEntry =
    HistoryEntry.Scan(
        uid = ID_NEW_ENTRY,
        value = this.data,
        creationDate = Instant.now(),
        format = this.format,
        fromImageFile = this.source == ScanSource.IMAGE_FILE,
    )
