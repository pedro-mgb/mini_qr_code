package com.pedroid.qrcodecompose.androidapp.features.history.data

enum class HistoryType {
    SCAN,
    GENERATE,
    ;

    companion object {
        fun fromRawString(str: String?): HistoryType = HistoryType.entries.first { str == it.name }
    }
}
