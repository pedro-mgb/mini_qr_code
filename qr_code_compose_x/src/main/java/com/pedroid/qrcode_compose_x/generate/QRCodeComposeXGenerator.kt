package com.pedroid.qrcode_compose_x.generate

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.Dp
import com.pedroid.qrcode_compose_x.generate.internal.DEFAULT_PADDING
import com.pedroid.qrcode_compose_x.generate.internal.DEFAULT_QR_CODE_SIZE_PX
import com.pedroid.qrcode_compose_x.generate.internal.generateQRCodeViaZxing

@Composable
fun QRCodeComposeXGenerator(
    modifier: Modifier = Modifier,
    text: String,
    onResult: (QRCodeGenerateResult) -> Unit,
    alignment: Alignment = Alignment.Center,
    qrCodePadding: Dp = DEFAULT_PADDING
) {
    val generateResult = remember(key1 = text) {
        generateQRCodeViaZxing(text = text, size = DEFAULT_QR_CODE_SIZE_PX)
    }
    Box(
        modifier = modifier,
        contentAlignment = alignment
    ) {
        if (generateResult is QRCodeGenerateResult.Generated) {
            Image(
                modifier = Modifier
                    .matchParentSize()
                    .padding(qrCodePadding),
                bitmap = generateResult.bitmap.asImageBitmap(),
                contentDescription = "QRCode $text"
            )
        }
    }
    LaunchedEffect(key1 = generateResult) {
        onResult(generateResult)
    }
}