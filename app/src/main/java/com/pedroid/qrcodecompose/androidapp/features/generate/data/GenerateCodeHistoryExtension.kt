package com.pedroid.qrcodecompose.androidapp.features.generate.data

import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryRepository
import com.pedroid.qrcodecompose.androidapp.features.history.domain.ID_NEW_ENTRY
import java.time.Instant

suspend fun HistoryRepository.saveGeneratedCode(data: QRCodeGeneratingContent): Long {
    return this.addHistoryEntry(data.toHistoryEntry())
}

private fun QRCodeGeneratingContent.toHistoryEntry(): HistoryEntry =
    HistoryEntry.Generate(
        uid = ID_NEW_ENTRY,
        value = this.qrCodeText,
        creationDate = Instant.now(),
        format = this.format,
    )
