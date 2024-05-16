package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

fun <T, R> ViewModel.createStateFlow(
    originalFlow: Flow<T>,
    initialValue: R,
    mapper: suspend (T) -> R,
): StateFlow<R> =
    originalFlow.map(mapper).stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000L),
        initialValue,
    )

fun <T> ViewModel.createStateFlow(
    originalFlow: Flow<T>,
    initialValue: T,
): StateFlow<T> =
    createStateFlow(
        originalFlow,
        initialValue,
        mapper = { it },
    )
