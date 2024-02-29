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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppLargeTextBox
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecomposelib.generate.QRCodeComposeXGenerator
import com.pedroid.qrcodecomposelib.generate.QRCodeGenerateResult

val qrCodeCornerShape = RoundedCornerShape(16.dp)

// region screen composables
@Composable
fun GenerateQRCodeScreen(
    state: GenerateQRCodeContentState,
    onTextUpdated: (String) -> Unit,
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
                onTextUpdated = onTextUpdated,
            )
        } else {
            GeneratedQRCodeContentPortrait(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                onTextUpdated = onTextUpdated,
            )
        }
    }
}

@Composable
fun GeneratedQRCodeContentLargeScreen(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    onTextUpdated: (String) -> Unit,
) {
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (textBox, qrCodeImage) = createRefs()
        QRAppLargeTextBox(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .padding(end = 40.dp)
                    .constrainAs(textBox) {
                        start.linkTo(parent.start)
                        end.linkTo(qrCodeImage.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints
                    },
            textValue = state.inputText,
            onTextChanged = onTextUpdated,
            label = stringResource(id = R.string.generate_code_text_box_label),
            imeAction = ImeAction.Done,
        )

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
                        start.linkTo(textBox.end)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
            qrCodeText = state.qrCodeText,
            onResultUpdate = {
                // TODO inform ViewModel of error or store bitmap to be shared
            },
        )
    }
}

@Composable
private fun GeneratedQRCodeContentPortrait(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    onTextUpdated: (String) -> Unit,
) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QRCodeImageOrInfoScreen(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.6f)
                    .aspectRatio(1f)
                    .border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = qrCodeCornerShape,
                    ),
            qrCodeText = state.qrCodeText,
            onResultUpdate = {
                // TODO inform ViewModel of error or store bitmap to be shared
            },
        )

        QRAppLargeTextBox(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(20.dp),
            textValue = state.inputText,
            onTextChanged = onTextUpdated,
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
// endregion screen composables

// region screen previews
@Preview
@Composable
fun GenerateQRCodeScreenPreview() {
    BaseQRCodeAppPreview {
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("qrCode", "qrCode"),
            onTextUpdated = { },
            largeScreen = false,
        )
    }
}

@Preview(name = "tablet", device = "spec:shape=Normal,width=1280,height=800,unit=dp,dpi=480")
@Composable
fun GenerateQRCodeScreenTabledPreview() {
    BaseQRCodeAppPreview {
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("qrCode large Screen", "qrCode Large Screen"),
            onTextUpdated = { },
            largeScreen = true,
        )
    }
}
// endregion screen previews
