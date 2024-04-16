package com.pedroid.qrcodecompose.androidapp.bridge.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.generate.QRCodeComposeXGenerator
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

private val qrCodeCornerShape = RoundedCornerShape(Dimens.roundedCornerLarge)

@Composable
fun QRCodeImageOrInfoScreen(
    modifier: Modifier = Modifier,
    showInfoScreen: Boolean,
    error: Boolean,
    qrCodeText: String,
    format: QRCodeComposeXFormat,
    onResultUpdate: (QRCodeGenerateResult) -> Unit,
) {
    Box(modifier = modifier) {
        if (showInfoScreen) {
            Box(
                modifier = Modifier.baseQRCodeImageModifier(format.preferredAspectRatio, error),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.padding(Dimens.spacingMedium),
                    text = stringResource(id = R.string.generate_code_will_appear_here),
                    textAlign = TextAlign.Center,
                )
            }
        } else {
            QRCodeComposeXGenerator(
                modifier =
                    Modifier.baseQRCodeImageModifier(format.preferredAspectRatio, error)
                        .background(
                            color = Color.White,
                            shape = qrCodeCornerShape,
                        ),
                alignment = Alignment.Center,
                text = qrCodeText,
                format = format,
                onResult = onResultUpdate,
            )
        }
    }
}

@Composable
private fun Modifier.baseQRCodeImageModifier(
    aspectRatio: Float,
    error: Boolean,
): Modifier =
    this then
        fillMaxWidth()
            .aspectRatio(aspectRatio)
            .border(
                width = Dimens.borderWidthMedium,
                color =
                    if (error) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onBackground
                    },
                shape = qrCodeCornerShape,
            )
