package com.pedroid.qrcodecompose.androidapp.designsystem.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppBackground
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.QRCodeComposeCameraXTheme

/**
 * Duplicating QR Code App Theme and Background, used in preview
 *
 * Usefulness:
 *  1. In case there's need to add configurability to preview, we can use change this method to avoid changing actual production code composables
 *  2. The usages of the theme / background for preview don't get mixed with actual usages of the theme / background in the app's production code
 *      helps when searching for usages in IDE
 */
@Composable
fun BaseQRCodeAppPreview(
    modifier: Modifier = Modifier,
    previewContent: @Composable () -> Unit,
) {
    QRCodeComposeCameraXTheme {
        QRAppBackground(
            modifier,
            content = previewContent,
        )
    }
}

@ExperimentalSharedTransitionApi
@Composable
fun BaseQRCodeAppWithAnimationPreview(
    modifier: Modifier = Modifier,
    previewContent: @Composable (SharedTransitionScope, AnimatedVisibilityScope) -> Unit,
) {
    BaseQRCodeAppPreview(modifier = modifier) {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                previewContent(this@SharedTransitionLayout, this@AnimatedVisibility)
            }
        }
    }
}
