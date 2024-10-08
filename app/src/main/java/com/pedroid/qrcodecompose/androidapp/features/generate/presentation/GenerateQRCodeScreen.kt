package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.QRCodeImageOrInfoContent
import com.pedroid.qrcodecompose.androidapp.core.presentation.getWindowSizeClassInPreview
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppTextBox
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.ContentCopy
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.SaveAlt
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppWithAnimationPreview
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.presentation.expandSharedElementTransition
import com.pedroid.qrcodecompose.androidapp.features.generate.data.QRCodeGeneratingContent
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.GenerateQRCodeActionListeners
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.GeneratedQRCodeUpdateListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

// region screen composables
@ExperimentalSharedTransitionApi
@Composable
fun GenerateQRCodeScreen(
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
    largeScreen: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .imePadding()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spacingMedium),
            text = stringResource(id = R.string.generate_code_header),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        QRCodeCustomization(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable { qrCodeActionListeners.onCustomize() }
                    .padding(horizontal = Dimens.spacingLarge),
            state = state,
            qrCodeActionListeners = qrCodeActionListeners,
            largeScreen = largeScreen,
        )
        if (largeScreen) {
            GeneratedQRCodeContentLargeScreen(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                qrCodeUpdateListeners = qrCodeUpdateListeners,
                qrCodeActionListeners = qrCodeActionListeners,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        } else {
            GeneratedQRCodeContentPortrait(
                modifier = Modifier.fillMaxWidth(),
                state = state,
                qrCodeUpdateListeners = qrCodeUpdateListeners,
                qrCodeActionListeners = qrCodeActionListeners,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
            )
        }
    }
}

@Composable
private fun QRCodeCustomization(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
    largeScreen: Boolean,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text =
                stringResource(
                    R.string.generate_code_current_customization_options,
                    stringResource(state.generating.format.titleStringId),
                ),
        )
        TextButton(onClick = { qrCodeActionListeners.onCustomize() }) {
            Text(text = stringResource(id = R.string.generate_code_customize_action))
        }
    }
}

@Composable
@ExperimentalSharedTransitionApi
private fun GeneratedQRCodeContentLargeScreen(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val actionButtonsVisibilityAlpha by rememberActionButtonsTransparency(state)
    val expandArguments =
        ExpandQRCodeArguments(
            code = state.generating.qrCodeText,
            format = state.generating.format,
            key = GENERATE_SHARED_TRANSITION_KEY,
        )
    ConstraintLayout(
        modifier = modifier,
    ) {
        val (textBox, qrCodeImage, actionButtons) = createRefs()
        QRCodeGenerateTextInput(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.4f)
                    .padding(end = Dimens.spacingExtraLarge)
                    .constrainAs(textBox) {
                        linkTo(start = parent.start, end = qrCodeImage.start)
                        linkTo(top = parent.top, bottom = parent.bottom)
                        height = Dimension.fillToConstraints
                    },
            state = state,
            qrCodeUpdateListeners = qrCodeUpdateListeners,
            largeScreen = true,
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
            Column(modifier = Modifier.padding(vertical = Dimens.spacingLarge, horizontal = Dimens.spacingSmall)) {
                QRCodeActionButtons(actionListeners = qrCodeActionListeners)
            }
        }

        QRCodeImageOrInfoContent(
            modifier =
                Modifier
                    .expandSharedElementTransition(
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        key = expandArguments.key,
                    )
                    .clickable(enabled = state.canGenerate) { qrCodeActionListeners.onExpand(expandArguments) }
                    .fillMaxWidth(fraction = 0.4f)
                    .constrainAs(qrCodeImage) {
                        linkTo(start = textBox.end, end = actionButtons.start)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
            showInfoScreen = !state.canGenerate,
            error = state.inputError,
            qrCodeText = state.generating.qrCodeText,
            format = state.generating.format,
            onResultUpdate = qrCodeUpdateListeners.onGeneratorResult,
        )
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun GeneratedQRCodeContentPortrait(
    modifier: Modifier = Modifier,
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    qrCodeActionListeners: GenerateQRCodeActionListeners,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val actionButtonsVisibilityAlpha by rememberActionButtonsTransparency(state)
    val expandArguments =
        ExpandQRCodeArguments(
            code = state.generating.qrCodeText,
            format = state.generating.format,
            key = GENERATE_SHARED_TRANSITION_KEY,
        )
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (qrCodeImage, actionButtons) = createRefs()
            QRCodeImageOrInfoContent(
                modifier =
                    Modifier
                        .expandSharedElementTransition(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            key = expandArguments.key,
                        )
                        .clickable(enabled = state.canGenerate) { qrCodeActionListeners.onExpand(expandArguments) }
                        .fillMaxWidth(fraction = 0.6f)
                        .constrainAs(qrCodeImage) {
                            linkTo(start = parent.start, end = parent.end)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                showInfoScreen = !state.canGenerate,
                error = state.inputError,
                qrCodeText = state.generating.qrCodeText,
                format = state.generating.format,
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
                Column(
                    modifier =
                        Modifier.padding(
                            vertical = Dimens.spacingMedium,
                            horizontal = Dimens.spacingExtraSmall,
                        ),
                ) {
                    QRCodeActionButtons(actionListeners = qrCodeActionListeners)
                }
            }
        }
        QRCodeGenerateTextInput(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.spacingMedium, vertical = Dimens.spacingSmall),
            state = state,
            qrCodeUpdateListeners = qrCodeUpdateListeners,
            largeScreen = false,
        )
    }
}

@Composable
private fun QRCodeGenerateTextInput(
    modifier: Modifier,
    state: GenerateQRCodeContentState,
    qrCodeUpdateListeners: GeneratedQRCodeUpdateListeners,
    largeScreen: Boolean,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (state.inputError) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = state.inputErrorMessage,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.error,
            )
        }
        QRAppTextBox(
            modifier =
                if (largeScreen) {
                    Modifier.fillMaxWidth().fillMaxHeight()
                } else {
                    Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                },
            textValue = state.inputText,
            onTextChanged = qrCodeUpdateListeners.onTextUpdated,
            label = stringResource(id = R.string.generate_code_text_box_label),
            imeAction = ImeAction.Done,
            keyboardType = state.generating.format.inputType,
            isError = state.inputError,
        )
    }
}

@Composable
private fun QRCodeActionButtons(actionListeners: GenerateQRCodeActionListeners = GenerateQRCodeActionListeners()) {
    IconButton(actionListeners.onImageSaveToFile) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
            imageVector = Icons.Outlined.SaveAlt,
            contentDescription = stringResource(id = R.string.action_save_to_file),
        )
    }
    Spacer(modifier = Modifier.size(Dimens.spacingMedium))
    IconButton(onClick = actionListeners.onImageShare) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
            imageVector = Icons.Filled.Share,
            contentDescription = stringResource(id = R.string.action_share),
        )
    }
    Spacer(modifier = Modifier.size(Dimens.spacingMedium))
    IconButton(onClick = actionListeners.onImageCopyToClipboard) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
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
    remember(key1 = state.canGenerate) {
        derivedStateOf {
            if (state.canGenerate) 1f else 0f
        }
    }

private const val GENERATE_SHARED_TRANSITION_KEY = "generating"
// endregion screen composables

// region screen previews
@ExperimentalSharedTransitionApi
@Preview
@Composable
fun GenerateQRCodeEmptyScreenPreview() {
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("", "", QRCodeGeneratingContent()),
            qrCodeUpdateListeners = GeneratedQRCodeUpdateListeners(),
            qrCodeActionListeners = GenerateQRCodeActionListeners(),
            largeScreen = false,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@ExperimentalSharedTransitionApi
@Preview
@Composable
fun GenerateQRCodeInputErrorPreview() {
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("bad input", "There is an error", QRCodeGeneratingContent()),
            qrCodeUpdateListeners = GeneratedQRCodeUpdateListeners(),
            qrCodeActionListeners = GenerateQRCodeActionListeners(),
            largeScreen = false,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@ExperimentalSharedTransitionApi
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@PreviewScreenSizes
@Composable
fun GenerateQRCodeWithContentScreenPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        GenerateQRCodeScreen(
            state = GenerateQRCodeContentState("qrCode", "", QRCodeGeneratingContent("some code", QRCodeComposeXFormat.QR_CODE)),
            qrCodeUpdateListeners = GeneratedQRCodeUpdateListeners(),
            qrCodeActionListeners = GenerateQRCodeActionListeners(),
            largeScreen = !phoneUI,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}
// endregion screen previews
