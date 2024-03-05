package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppTextBox
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.ContentCopy
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.SaveAlt
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.getWindowSizeClassInPreview
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.GenerateQRCodeActionListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.GeneratedQRCodeUpdateListeners
import com.pedroid.qrcodecomposelib.generate.QRCodeComposeXGenerator
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

val qrCodeCornerShape = RoundedCornerShape(16.dp)

// region screen composables
@Composable
fun GenerateQRCodeScreen(
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
    largeScreen: Boolean = false,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
            text = stringResource(id = R.string.generate_code_header),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(20.dp))
        if (largeScreen) {
            GeneratedQRCodeContentLargeScreen(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                qrCodeUpdateListeners = qrCodeUpdateListeners,
                qrCodeActionListeners = qrCodeActionListeners,
            )
        } else {
            GeneratedQRCodeContentPortrait(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                qrCodeUpdateListeners = qrCodeUpdateListeners,
                qrCodeActionListeners = qrCodeActionListeners,
            )
        }
    }
}

@Composable
fun GeneratedQRCodeContentLargeScreen(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
) {
    val actionButtonsVisibilityAlpha by rememberActionButtonsTransparency(state)
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (textBox, qrCodeImage, actionButtons) = createRefs()
        QRAppTextBox(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .padding(end = 40.dp)
                    .constrainAs(textBox) {
                        linkTo(start = parent.start, end = qrCodeImage.start)
                        linkTo(top = parent.top, bottom = parent.bottom)
                        height = Dimension.fillToConstraints
                    },
            textValue = state.inputText,
            onTextChanged = qrCodeUpdateListeners.onTextUpdated,
            label = stringResource(id = R.string.generate_code_text_box_label),
            imeAction = ImeAction.Done,
        )

        Card(
            modifier =
                Modifier
                    .alpha(actionButtonsVisibilityAlpha)
                    .constrainAs(actionButtons) {
                        linkTo(start = qrCodeImage.end, end = parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
        ) {
            Column(modifier = Modifier.padding(vertical = 32.dp, horizontal = 10.dp)) {
                QRCodeActionButtons(actionListeners = qrCodeActionListeners)
            }
        }

        QRCodeImageOrInfoScreen(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .aspectRatio(1f)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = qrCodeCornerShape,
                    )
                    .constrainAs(qrCodeImage) {
                        linkTo(start = textBox.end, end = actionButtons.start)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
            qrCodeText = state.qrCodeText,
            onResultUpdate = qrCodeUpdateListeners.onGeneratorResult,
        )
    }
}

@Composable
private fun GeneratedQRCodeContentPortrait(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
) {
    val actionButtonsVisibilityAlpha by rememberActionButtonsTransparency(state)
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (qrCodeImage, actionButtons) = createRefs()
            QRCodeImageOrInfoScreen(
                modifier =
                    Modifier
                        .fillMaxWidth(fraction = 0.6f)
                        .aspectRatio(1f)
                        .border(
                            width = 4.dp,
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = qrCodeCornerShape,
                        ).constrainAs(qrCodeImage) {
                            linkTo(start = parent.start, end = parent.end)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                qrCodeText = state.qrCodeText,
                onResultUpdate = qrCodeUpdateListeners.onGeneratorResult,
            )
            Card(
                modifier =
                    Modifier
                        .alpha(actionButtonsVisibilityAlpha)
                        .constrainAs(actionButtons) {
                            linkTo(top = parent.top, bottom = parent.bottom)
                            linkTo(start = qrCodeImage.end, end = parent.end)
                        },
            ) {
                Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 4.dp)) {
                    QRCodeActionButtons(actionListeners = qrCodeActionListeners)
                }
            }
        }
        QRAppTextBox(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
            textValue = state.inputText,
            onTextChanged = qrCodeUpdateListeners.onTextUpdated,
            label = stringResource(id = R.string.generate_code_text_box_label),
            imeAction = ImeAction.Done,
        )
    }
}

@Composable
private fun QRCodeImageOrInfoScreen(
    modifier: Modifier = Modifier,
    qrCodeText: String,
    onResultUpdate: (QRCodeGenerateResult) -> Unit,
) {
    if (qrCodeText.isBlank()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = Modifier.padding(20.dp),
                text = stringResource(id = R.string.generate_code_will_appear_here),
                textAlign = TextAlign.Center,
            )
        }
    } else {
        QRCodeComposeXGenerator(
            modifier =
                modifier.background(
                    color = Color.White,
                    shape = qrCodeCornerShape,
                ),
            alignment = Alignment.Center,
            text = qrCodeText,
            onResult = onResultUpdate,
        )
    }
}

@Composable
private fun QRCodeActionButtons(actionListeners: GenerateQRCodeActionListeners = GenerateQRCodeActionListeners()) {
    IconButton(actionListeners.onImageSaveToFile) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Outlined.SaveAlt,
            contentDescription = stringResource(id = R.string.action_open),
        )
    }
    Spacer(modifier = Modifier.size(20.dp))
    IconButton(onClick = actionListeners.onImageShare) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.action_share),
        )
    }
    Spacer(modifier = Modifier.size(20.dp))
    IconButton(onClick = actionListeners.onImageCopyToClipboard) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = Icons.Outlined.ContentCopy,
            contentDescription = stringResource(id = R.string.action_copy),
        )
    }
}

/**
 * Utility function to remember through compositions, the alpha of action buttons
 *
 * - If QR Code is to be generated, buttons should visible
 * - If there is no QR Code, buttons should not
 *
 * The reason for this si to instead of not showing the buttons completely, it will hide them
 *  but the layout for it is still present
 * In terms of Android Views, we are employing View.INVISIBLE instead of View.GONE
 * The reason for this is to avoid layout size changes when inputting / erasing the qr code text
 *
 * @param state the state of the QR Code to generate
 */
@Composable
private fun rememberActionButtonsTransparency(state: GenerateQRCodeContentState): State<Float> =
    remember(key1 = state.qrCodeText) {
        derivedStateOf {
            if (state.qrCodeText.isNotBlank()) 1f else 0f
        }
    }
// endregion screen composables

// region screen previews
@Preview
@Composable
fun GenerateQRCodeEmptyScreenPreview() {
    BaseQRCodeAppPreview {
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("", ""),
            qrCodeUpdateListeners = GeneratedQRCodeUpdateListeners(),
            qrCodeActionListeners = GenerateQRCodeActionListeners(),
            largeScreen = false,
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@PreviewScreenSizes
@Composable
fun GenerateQRCodeWithContentScreenPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppPreview {
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("qrCode", "qrCode"),
            qrCodeUpdateListeners = GeneratedQRCodeUpdateListeners(),
            qrCodeActionListeners = GenerateQRCodeActionListeners(),
            largeScreen = !phoneUI,
        )
    }
}
// endregion screen previews
