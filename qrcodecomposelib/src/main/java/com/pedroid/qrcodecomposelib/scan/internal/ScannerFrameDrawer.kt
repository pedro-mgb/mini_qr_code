package com.pedroid.qrcodecomposelib.scan.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

fun Modifier.drawScannerFrame(
    color: Color,
    verticalMarginPercent: Float = 0.5f,
) = this.drawWithCache {
        onDrawWithContent {
            val squareFrameSize = Size(size.minDimension / 1.5f, size.minDimension / 1.5f)
            val frameOffset =
                Offset(
                    (size.width - squareFrameSize.width) * 0.5f,
                    (size.height - squareFrameSize.height) * verticalMarginPercent.coerceIn(0.0f, 1.0f),
                )
            drawRoundRect(
                color = color,
                topLeft = frameOffset,
                size = squareFrameSize,
                style = Stroke(width = 16.0f),
                cornerRadius = CornerRadius(32.0f),
            )
        }
    }

@Preview
@Composable
fun PreviewFrameFullScreen() {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(20.dp),
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .drawScannerFrame(
                        color = Color.Green,
                        verticalMarginPercent = 0.4f,
                    ),
        )
    }
}
