package com.pedroid.qrcodecompose.androidapp.features.expand.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppWithAnimationPreview
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.generate.QRCodeComposeXGenerator

// region screen composables
@ExperimentalSharedTransitionApi
@Composable
fun ExpandQRCodeScreen(
    arguments: ExpandQRCodeArguments,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigationListeners: ExpandQRCodeNavigationListeners,
) {
    with(sharedTransitionScope) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .clickable { navigationListeners.goBack },
        ) {
            QRCodeComposeXGenerator(
                modifier =
                    Modifier.Companion
                        .sharedElement(
                            state = sharedTransitionScope.rememberSharedContentState(key = arguments.toString()),
                            animatedVisibilityScope = animatedVisibilityScope,
                        )
                        .fillMaxWidth(fraction = 0.9f)
                        .align(Alignment.Center),
                text = arguments.code,
                format = arguments.format,
                onResult = {
                    // nothing to handle here; should just display image
                },
            )
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewScreenSizes
@ExperimentalSharedTransitionApi
@Composable
fun ExpandQRCodeScreenPreview() {
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        ExpandQRCodeScreen(
            arguments =
                ExpandQRCodeArguments(
                    label = "generated",
                    code = "123467",
                    format = QRCodeComposeXFormat.BARCODE_US_UPC_E,
                ),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            navigationListeners = ExpandQRCodeNavigationListeners(),
        )
    }
}
// endregion screen previews
