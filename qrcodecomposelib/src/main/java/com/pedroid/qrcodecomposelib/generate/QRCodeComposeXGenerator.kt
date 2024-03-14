package com.pedroid.qrcodecomposelib.generate

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
import com.pedroid.qrcodecomposelib.generate.internal.DEFAULT_PADDING
import com.pedroid.qrcodecomposelib.generate.internal.DEFAULT_QR_CODE_SIZE_PX
import com.pedroid.qrcodecomposelib.generate.internal.DEFAULT_QR_CODE_TEXT_ENCODING
import com.pedroid.qrcodecomposelib.generate.internal.generateQRCodeViaZxing

@Composable
fun QRCodeComposeXGenerator(
    modifier: Modifier = Modifier,
    text: String,
    onResult: (QRCodeGenerateResult) -> Unit,
    alignment: Alignment = Alignment.Center,
    qrCodePadding: Dp = DEFAULT_PADDING,
    qrCodeImageSizePx: Int = DEFAULT_QR_CODE_SIZE_PX,
    qrCodeAccessibilityContentDescription: String = text,
    qrCodeTextEncoding: String = DEFAULT_QR_CODE_TEXT_ENCODING,
) {
    val generateResult =
        remember(key1 = text, key2 = qrCodeImageSizePx, key3 = qrCodeTextEncoding) {
            generateQRCodeViaZxing(
                text = text,
                size = qrCodeImageSizePx,
                encoding = qrCodeTextEncoding,
            )
        }
    Box(
        modifier = modifier,
        contentAlignment = alignment,
    ) {
        if (generateResult is QRCodeGenerateResult.Generated) {
            Image(
                modifier =
                    Modifier
                        .matchParentSize()
                        .padding(qrCodePadding),
                bitmap = generateResult.bitmap.asImageBitmap(),
                contentDescription = qrCodeAccessibilityContentDescription,
            )
        }
    }
    LaunchedEffect(key1 = generateResult) {
        onResult(generateResult)
    }
}
