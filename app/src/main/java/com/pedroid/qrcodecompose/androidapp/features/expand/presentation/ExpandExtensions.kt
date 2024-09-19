package com.pedroid.qrcodecompose.androidapp.features.expand.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@ExperimentalSharedTransitionApi
@Composable
fun Modifier.expandSharedElementTransition(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    key: Any,
): Modifier {
    return with(sharedTransitionScope) {
        this@expandSharedElementTransition.sharedElement(
            state = rememberSharedContentState(key = key),
            animatedVisibilityScope = animatedVisibilityScope,
            boundsTransform = { _, _ ->
                spring(stiffness = Spring.StiffnessMediumLow)
            },
        )
    }
}
