package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle

/**
 * Helper method to update a value of [SavedStateHandle] based on its current value.
 * Will update if the value returned by updateDelegate is not null
 */
@MainThread
fun <T> SavedStateHandle.update(
    key: String,
    updateDelegate: (T?) -> T?
) {
    updateDelegate(this[key])?.let {
        this[key] = it
    }
}