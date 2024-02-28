package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

fun WindowSizeClass.showPhoneUI() = this.widthSizeClass.showPhoneUI()

fun WindowWidthSizeClass.showPhoneUI() = this == WindowWidthSizeClass.Compact
