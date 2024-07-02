package com.pedroid.qrcodecompose.androidapp.features.expand.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppWithAnimationPreview
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.generate.QRCodeComposeXGenerator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val MIN_ZOOM = 1f
private const val MAX_ZOOM = 2f
private const val IMAGE_WIDTH_FACTOR = 0.9f
private val PADDING = 16.dp

// region screen composables
@ExperimentalSharedTransitionApi
@Composable
fun ExpandQRCodeScreen(
    arguments: ExpandQRCodeArguments,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    navigationListeners: ExpandQRCodeNavigationListeners,
) {
    val aspectRatio = arguments.format.preferredAspectRatio
    val pixelPaddingDiscount = with(LocalDensity.current) { PADDING.toPx() }
    var imageScale by remember { mutableFloatStateOf(1f) }
    var imageOffset by remember { mutableStateOf(Offset.Zero) }
    var closeButtonVisible by remember { mutableStateOf(true) }
    val closeButtonAlpha: Float by animateFloatAsState(
        if (closeButtonVisible) 1f else 0f,
        label = "expandQRCodeCloseButton+${arguments.key}",
    )
    val coroutineScope = rememberCoroutineScope()
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val transformableState =
            rememberTransformableState(
                currentScale = imageScale,
                currentOffset = imageOffset,
                pixelPaddingDiscount = pixelPaddingDiscount,
                aspectRatio = aspectRatio,
                constraints = constraints,
                updateScale = { imageScale = it },
                updateOffset = { imageOffset = it },
            )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = imageScale,
                        scaleY = imageScale,
                        rotationZ = 0f,
                        translationX = imageOffset.x,
                        translationY = imageOffset.y,
                    )
                    .pointerInput(key1 = Unit) {
                        detectTapGestures(
                            onTap = {
                                closeButtonVisible = !closeButtonVisible
                            },
                            onDoubleTap = {
                                coroutineScope.launch {
                                    animateDoubleTapZoom(
                                        currentScale = imageScale,
                                        currentOffset = imageOffset,
                                        updateScale = { imageScale = it },
                                        updateOffset = { imageOffset = it },
                                    )
                                }
                            },
                        )
                    }
                    .transformable(state = transformableState),
        ) {
            QRCodeComposeXGenerator(
                modifier =
                    Modifier
                        .expandSharedElementTransition(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            key = arguments.key,
                        )
                        .aspectRatio(aspectRatio)
                        .background(Color.White)
                        .align(Alignment.Center)
                        .fillMaxWidth(fraction = IMAGE_WIDTH_FACTOR),
                text = arguments.code,
                format = arguments.format,
                qrCodePadding = PADDING,
                onResult = {
                    // nothing to handle here; should just display image
                },
            )
        }

        IconButton(
            modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .graphicsLayer(alpha = closeButtonAlpha)
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .padding(Dimens.spacingMedium)
                    .background(
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        shape = CircleShape,
                    )
                    .size(Dimens.iconButtonSize + Dimens.spacingSmall),
            onClick = navigationListeners.goBack,
        ) {
            Icon(
                modifier = Modifier.size(Dimens.iconButtonSize),
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(id = R.string.action_close),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

@Composable
private fun rememberTransformableState(
    currentScale: Float,
    currentOffset: Offset,
    pixelPaddingDiscount: Float,
    constraints: Constraints,
    aspectRatio: Float,
    updateScale: (Float) -> Unit,
    updateOffset: (Offset) -> Unit,
) = rememberTransformableState { zoomChange, offsetChange, _ ->
    val imageScale = (currentScale * zoomChange).coerceIn(MIN_ZOOM, MAX_ZOOM)
    if (imageScale > MIN_ZOOM) {
        val maxOffsetX =
            ((constraints.maxWidth * (imageScale - 1) * IMAGE_WIDTH_FACTOR) / 2 - pixelPaddingDiscount)
                .coerceIn(0f, Float.POSITIVE_INFINITY)
        val maxOffsetY =
            ((constraints.maxWidth * (imageScale - 1) / aspectRatio * IMAGE_WIDTH_FACTOR) / 2 - pixelPaddingDiscount)
                .coerceIn(0f, Float.POSITIVE_INFINITY)
        val imageOffset =
            Offset(
                x = (currentOffset.x + (offsetChange.x * imageScale)).coerceIn(-maxOffsetX, maxOffsetX),
                y = (currentOffset.y + (offsetChange.y * imageScale)).coerceIn(-maxOffsetY, maxOffsetY),
            )
        updateScale(imageScale)
        updateOffset(imageOffset)
    } else {
        updateOffset(Offset.Zero)
    }
}

private suspend fun animateDoubleTapZoom(
    currentScale: Float,
    currentOffset: Offset,
    updateScale: (Float) -> Unit,
    updateOffset: (Offset) -> Unit,
) {
    val animationSteps = 10
    val newScale =
        if (currentScale <= MIN_ZOOM * 1.1f) {
            // the 1.1f is to also zoomIn when the image is slightly zoomed-in but is barely noticeable
            (MAX_ZOOM + MIN_ZOOM) / 2
        } else {
            MIN_ZOOM
        }
    val newOffset = 0f
    for (i in 1..animationSteps) {
        val scaleStep = (newScale - currentScale) * i / animationSteps
        if (scaleStep != 0f) {
            updateScale(currentScale + scaleStep)
        }
        if (currentOffset != Offset.Zero) {
            val offsetStepX = (newOffset - currentOffset.x) * i / animationSteps
            val offsetStepY = (newOffset - currentOffset.y) * i / animationSteps
            updateOffset(currentOffset + Offset(x = offsetStepX, y = offsetStepY))
        }
        // small delay to give the visual of an animation
        delay(timeMillis = 20L)
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
                    key = "generated",
                    code = "1234678",
                    format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8,
                ),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            navigationListeners = ExpandQRCodeNavigationListeners(),
        )
    }
}
// endregion screen previews
