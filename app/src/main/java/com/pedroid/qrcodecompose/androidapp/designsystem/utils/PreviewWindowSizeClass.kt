package com.pedroid.qrcodecompose.androidapp.designsystem.utils

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * Alternative to [androidx.compose.material3.adaptive.currentWindowAdaptiveInfo]
 *
 * The currentWindowAdaptiveInfo for some reason is not working properly with the @PreviewScreenSizes annotation
 * [androidx.compose.ui.tooling.preview.PreviewScreenSizes];
 * But with this method we can accurately get the window size class to display largeScreen content.
 * Since material3.adaptive is in alpha, perhaps the method will be fixed soon.
 */
@ExperimentalMaterial3WindowSizeClassApi
@Composable
fun getWindowSizeClassInPreview(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    return WindowSizeClass.calculateFromSize(size)
}
