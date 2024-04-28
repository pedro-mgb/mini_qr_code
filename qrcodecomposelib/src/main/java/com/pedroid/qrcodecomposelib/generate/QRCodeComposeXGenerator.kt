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
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.common.internal.toZxingBarcodeFormat
import com.pedroid.qrcodecomposelib.generate.internal.DEFAULT_PADDING
import com.pedroid.qrcodecomposelib.generate.internal.DEFAULT_QR_CODE_SIZE_PX
import com.pedroid.qrcodecomposelib.generate.internal.DEFAULT_QR_CODE_TEXT_ENCODING
import com.pedroid.qrcodecomposelib.generate.internal.generateCodeViaZxing

/**
 * Composable function to generate and display a QR Code or barcode; customizable
 *
 * @param modifier a Compose Modifier
 * @param text the actual QR Code text content to be generated
 * @param onResult listener for when generate result is updated - image generated or error occured
 * @param format the [com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat] - QR Code, Aztec, Barcode EAN-13, etc.
 * @param alignment which kind of alignment should the composable have (Center by default)
 * @param qrCodePadding the padding in dp that the composable should have
 * @param qrCodeImageSizePx the image size in pixels for the QR Code; if the aspect ratio (specified in the format) is not 1:1, then it's the width.
 *                          the larger the image size, the better quality but also the larger file size. Default is 1000px.
 * @param qrCodeAccessibilityContentDescription accessibility description for software like screen readers - by default the QR Code text
 * @param qrCodeTextEncoding the encoding to use on the text, by default UTF-8
 */
@Composable
fun QRCodeComposeXGenerator(
    modifier: Modifier = Modifier,
    text: String,
    onResult: (QRCodeGenerateResult) -> Unit,
    format: QRCodeComposeXFormat = QRCodeComposeXFormat.QR_CODE,
    alignment: Alignment = Alignment.Center,
    qrCodePadding: Dp = DEFAULT_PADDING,
    qrCodeImageSizePx: Int = DEFAULT_QR_CODE_SIZE_PX,
    qrCodeAccessibilityContentDescription: String = text,
    qrCodeTextEncoding: String = DEFAULT_QR_CODE_TEXT_ENCODING,
) {
    val generateResult =
        remember(text, format, qrCodeImageSizePx, qrCodeTextEncoding) {
            generateCodeViaZxing(
                text = text,
                width = qrCodeImageSizePx,
                aspectRatio = format.preferredAspectRatio,
                format = format.toZxingBarcodeFormat(),
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
