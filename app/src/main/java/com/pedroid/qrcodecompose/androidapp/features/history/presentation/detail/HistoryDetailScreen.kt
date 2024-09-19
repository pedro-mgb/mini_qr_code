package com.pedroid.qrcodecompose.androidapp.features.history.presentation.detail

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutBaseScope
import androidx.constraintlayout.compose.Dimension
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.QRCodeImageOrInfoContent
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.QRCodeTextContent
import com.pedroid.qrcodecompose.androidapp.core.presentation.getWindowSizeClassInPreview
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppSimpleToolbar
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.ContentCopy
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppWithAnimationPreview
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeArguments
import com.pedroid.qrcodecompose.androidapp.features.expand.presentation.expandSharedElementTransition
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HistoryDetailActionListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HistoryDetailNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.HistoryTypeUI
import com.pedroid.qrcodecompose.androidapp.features.history.presentation.delete.deleteButtonColors
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

// region screen composables
@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun HistoryDetailScreen(
    data: HistoryDetail,
    largeScreen: Boolean,
    navigationListeners: HistoryDetailNavigationListeners,
    actionListeners: HistoryDetailActionListeners,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QRAppSimpleToolbar(title = stringResource(R.string.history_top_header_title), onNavigationIconClick = navigationListeners.onGoBack)
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            HistoryDetailHeader(data = data)
            Spacer(modifier = Modifier.size(Dimens.spacingMedium))
            if (largeScreen) {
                HistoryDetailContentLargeScreen(
                    modifier = Modifier.fillMaxWidth(),
                    data = data,
                    navigationListeners = navigationListeners,
                    actionListeners = actionListeners,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            } else {
                HistoryDetailContentPortrait(
                    modifier = Modifier.fillMaxWidth(),
                    data = data,
                    navigationListeners = navigationListeners,
                    actionListeners = actionListeners,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }
            Button(
                modifier = Modifier.padding(vertical = Dimens.spacingMedium),
                colors = deleteButtonColors(),
                onClick = actionListeners.onDeleteItem,
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                Text(
                    modifier = Modifier.padding(horizontal = Dimens.spacingMedium),
                    text = stringResource(id = R.string.history_delete_action),
                )
            }
        }
    }
}

@Composable
private fun HistoryDetailHeader(data: HistoryDetail) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(Dimens.spacingExtraSmall),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(Dimens.iconButtonSize),
            imageVector = data.typeUI.icon,
            contentDescription = null,
            tint = data.typeUI.color(),
        )
        Spacer(modifier = Modifier.size(Dimens.spacingExtraSmall))
        Text(
            text = stringResource(id = data.typeUI.typeDetailsStringId),
            style = MaterialTheme.typography.titleMedium,
            color = data.typeUI.color(),
        )
    }
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = data.displayDate,
        textAlign = TextAlign.Center,
    )
}

@ExperimentalSharedTransitionApi
@Composable
private fun HistoryDetailContentLargeScreen(
    modifier: Modifier = Modifier,
    data: HistoryDetail,
    navigationListeners: HistoryDetailNavigationListeners,
    actionListeners: HistoryDetailActionListeners,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val expandArguments =
        ExpandQRCodeArguments(
            code = data.value,
            format = data.format,
            key = HISTORY_SHARED_TRANSITION_KEY,
        )

    ConstraintLayout(modifier = modifier) {
        val (text, actionButtonsText, errorText, image, actionButtonsImage) = createRefs()

        @Composable
        fun TextLargeScreenContent(
            startAnchor: ConstrainScope.() -> ConstraintLayoutBaseScope.VerticalAnchor,
            endAnchor: ConstrainScope.() -> ConstraintLayoutBaseScope.VerticalAnchor,
        ) {
            QRCodeTextContent(
                modifier =
                    Modifier
                        .fillMaxWidth(0.4f)
                        .heightIn(min = 200.dp, max = 500.dp)
                        .constrainAs(text) {
                            linkTo(start = startAnchor(), end = endAnchor())
                            top.linkTo(parent.top)
                        },
                qrCode = data.value,
            )

            HistoryDetailActionButtons(
                modifier =
                    Modifier
                        .padding(top = Dimens.spacingMedium)
                        .constrainAs(actionButtonsText) {
                            linkTo(start = text.start, end = text.end)
                            top.linkTo(text.bottom)
                        },
                shareClickListener = {
                    actionListeners.onTextShare(data.value)
                },
                copyClickListener = {
                    actionListeners.onTextCopyToClipboard(data.value)
                },
                largeScreen = true,
            )
        }

        @Composable
        fun ImageLargeScreenContent(
            startAnchor: ConstrainScope.() -> ConstraintLayoutBaseScope.VerticalAnchor,
            endAnchor: ConstrainScope.() -> ConstraintLayoutBaseScope.VerticalAnchor,
        ) {
            val actionButtonsVisibilityAlpha = if (!data.errorInGenerating) 1f else 0f
            if (data.errorInGenerating) {
                Text(
                    modifier =
                        Modifier
                            .padding(
                                start = Dimens.spacingLarge,
                                end = Dimens.spacingLarge,
                                bottom = Dimens.spacingExtraSmall,
                            )
                            .constrainAs(errorText) {
                                width = Dimension.preferredWrapContent
                                linkTo(start = image.start, end = image.end)
                                top.linkTo(parent.top)
                            },
                    text = stringResource(id = R.string.history_detail_preview_generation_failed),
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                Box(
                    modifier =
                        Modifier.constrainAs(errorText) {
                            linkTo(start = image.start, end = image.end)
                            top.linkTo(parent.top)
                        },
                )
            }

            QRCodeImageOrInfoContent(
                modifier =
                    Modifier
                        .expandSharedElementTransition(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            key = expandArguments.key,
                        )
                        .clickable { navigationListeners.onExpand(expandArguments) }
                        .fillMaxWidth(0.4f)
                        .constrainAs(image) {
                            linkTo(start = startAnchor(), end = endAnchor())
                            top.linkTo(errorText.bottom)
                        },
                showInfoScreen = false,
                error = data.errorInGenerating,
                qrCodeText = data.value,
                format = data.format,
                onResultUpdate = actionListeners.generateResult,
            )

            HistoryDetailActionButtons(
                modifier =
                    Modifier
                        .padding(top = Dimens.spacingMedium)
                        .alpha(actionButtonsVisibilityAlpha)
                        .constrainAs(actionButtonsImage) {
                            linkTo(start = image.start, end = image.end)
                            top.linkTo(image.bottom)
                        },
                shareClickListener = actionListeners.onImageShare,
                copyClickListener = actionListeners.onImageCopyToClipboard,
                largeScreen = true,
            )
        }

        if (data.typeUI == HistoryTypeUI.GENERATE) {
            ImageLargeScreenContent(
                startAnchor = { parent.start },
                endAnchor = { text.start },
            )
            TextLargeScreenContent(
                startAnchor = { image.end },
                endAnchor = { parent.end },
            )
        } else {
            TextLargeScreenContent(
                startAnchor = { parent.start },
                endAnchor = { image.start },
            )
            ImageLargeScreenContent(
                startAnchor = { text.end },
                endAnchor = { parent.end },
            )
        }
    }
}

@ExperimentalSharedTransitionApi
@Composable
private fun HistoryDetailContentPortrait(
    modifier: Modifier = Modifier,
    data: HistoryDetail,
    navigationListeners: HistoryDetailNavigationListeners,
    actionListeners: HistoryDetailActionListeners,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
) {
    val expandArguments =
        ExpandQRCodeArguments(
            code = data.value,
            format = data.format,
            key = HISTORY_SHARED_TRANSITION_KEY,
        )

    @Composable
    fun TextPortrait() {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (text, actionButtons) = createRefs()
            QRCodeTextContent(
                modifier =
                    Modifier
                        .fillMaxWidth(0.6f)
                        .heightIn(min = 175.dp, max = 300.dp)
                        .constrainAs(text) {
                            linkTo(start = parent.start, end = parent.end)
                            linkTo(top = parent.top, bottom = parent.bottom)
                        },
                qrCode = data.value,
            )

            HistoryDetailActionButtons(
                modifier =
                    Modifier.constrainAs(actionButtons) {
                        linkTo(start = text.end, end = parent.end)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
                shareClickListener = {
                    actionListeners.onTextShare(data.value)
                },
                copyClickListener = {
                    actionListeners.onTextCopyToClipboard(data.value)
                },
                largeScreen = false,
            )
        }
    }

    @Composable
    fun ImagePortrait() {
        val actionButtonsVisibilityAlpha = if (!data.errorInGenerating) 1f else 0f
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (errorText, image, actionButtons) = createRefs()

            if (data.errorInGenerating) {
                Text(
                    modifier =
                        Modifier
                            .padding(
                                start = Dimens.spacingLarge,
                                end = Dimens.spacingLarge,
                                bottom = Dimens.spacingExtraSmall,
                            )
                            .constrainAs(errorText) {
                                width = Dimension.preferredWrapContent
                                linkTo(start = parent.start, end = parent.end)
                                top.linkTo(parent.top)
                            },
                    text = stringResource(id = R.string.history_detail_preview_generation_failed),
                    color = MaterialTheme.colorScheme.error,
                )
            } else {
                Box(
                    modifier =
                        Modifier.constrainAs(errorText) {
                            linkTo(start = parent.start, end = parent.end)
                            top.linkTo(parent.top)
                        },
                )
            }

            QRCodeImageOrInfoContent(
                modifier =
                    Modifier
                        .expandSharedElementTransition(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            key = expandArguments.key,
                        )
                        .clickable { navigationListeners.onExpand(expandArguments) }
                        .fillMaxWidth(0.6f)
                        .constrainAs(image) {
                            linkTo(start = parent.start, end = parent.end)
                            linkTo(top = errorText.bottom, bottom = parent.bottom)
                        },
                showInfoScreen = false,
                error = data.errorInGenerating,
                qrCodeText = data.value,
                format = data.format,
                onResultUpdate = actionListeners.generateResult,
            )

            HistoryDetailActionButtons(
                modifier =
                    Modifier
                        .alpha(actionButtonsVisibilityAlpha)
                        .constrainAs(actionButtons) {
                            linkTo(start = image.end, end = parent.end)
                            linkTo(top = errorText.bottom, bottom = parent.bottom)
                        },
                shareClickListener = actionListeners.onImageShare,
                copyClickListener = actionListeners.onImageCopyToClipboard,
                largeScreen = false,
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (data.typeUI == HistoryTypeUI.GENERATE) {
            ImagePortrait()
            Spacer(modifier = Modifier.size(Dimens.spacingExtraLarge))
            TextPortrait()
        } else {
            TextPortrait()
            Spacer(modifier = Modifier.size(Dimens.spacingExtraLarge))
            ImagePortrait()
        }
    }
}

@Composable
private fun HistoryDetailActionButtons(
    modifier: Modifier = Modifier,
    shareClickListener: () -> Unit,
    copyClickListener: () -> Unit,
    largeScreen: Boolean,
) {
    @Composable
    fun IconButtonsContent() {
        IconButton(onClick = shareClickListener) {
            Icon(
                modifier = Modifier.size(Dimens.iconButtonSize),
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(id = R.string.action_share),
            )
        }
        Spacer(modifier = Modifier.size(Dimens.spacingMedium))
        IconButton(onClick = copyClickListener) {
            Icon(
                modifier = Modifier.size(Dimens.iconButtonSize),
                imageVector = Icons.Outlined.ContentCopy,
                contentDescription = stringResource(id = R.string.action_copy),
            )
        }
    }
    Card(modifier = modifier) {
        if (largeScreen) {
            Row(modifier = Modifier.padding(horizontal = Dimens.spacingLarge, vertical = Dimens.spacingSmall)) {
                IconButtonsContent()
            }
        } else {
            Column(modifier = Modifier.padding(vertical = Dimens.spacingLarge, horizontal = Dimens.spacingSmall)) {
                IconButtonsContent()
            }
        }
    }
}

private const val HISTORY_SHARED_TRANSITION_KEY = "history_detail"
// endregion screen composables

// region screen previews
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalSharedTransitionApi::class)
@PreviewScreenSizes
@Composable
fun HistoryDetailScreenScannedPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        HistoryDetailScreen(
            data =
                HistoryDetail(
                    uid = 1L,
                    value =
                        "generated qr code with a considerably long text\n" +
                            "that probably does not fit inside a single line\n" +
                            "and therefore may be ellipsized",
                    format = QRCodeComposeXFormat.QR_CODE,
                    displayDate = "Monday, April 15 2024 - 18:00:00",
                    typeUI = HistoryTypeUI.SCAN_CAMERA,
                ),
            largeScreen = !phoneUI,
            navigationListeners = HistoryDetailNavigationListeners(),
            actionListeners = HistoryDetailActionListeners(),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalSharedTransitionApi::class)
@PreviewScreenSizes
@Composable
fun HistoryDetailScreenGeneratedPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        HistoryDetailScreen(
            data =
                HistoryDetail(
                    uid = 2L,
                    value = "123456789",
                    format = QRCodeComposeXFormat.PDF_417,
                    displayDate = "Monday, April 15 2024 - 6:02:00 PM",
                    typeUI = HistoryTypeUI.GENERATE,
                ),
            largeScreen = !phoneUI,
            navigationListeners = HistoryDetailNavigationListeners(),
            actionListeners = HistoryDetailActionListeners(),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalSharedTransitionApi::class)
@PreviewScreenSizes
@Composable
fun HistoryDetailScreenScannedErrorPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        HistoryDetailScreen(
            data =
                HistoryDetail(
                    uid = 3L,
                    value = "123",
                    format = QRCodeComposeXFormat.BARCODE_ITF,
                    displayDate = "Monday, April 15 2024 - 18:00:00",
                    typeUI = HistoryTypeUI.SCAN_CAMERA,
                    errorInGenerating = true,
                ),
            largeScreen = !phoneUI,
            navigationListeners = HistoryDetailNavigationListeners(),
            actionListeners = HistoryDetailActionListeners(),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalSharedTransitionApi::class)
@PreviewScreenSizes
@Composable
fun HistoryDetailScreenGeneratedErrorPreview() {
    val phoneUI = getWindowSizeClassInPreview().showPhoneUI()
    BaseQRCodeAppWithAnimationPreview(modifier = Modifier.fillMaxSize()) { sharedTransitionScope, animatedVisibilityScope ->
        HistoryDetailScreen(
            data =
                HistoryDetail(
                    uid = 4L,
                    value = "12345678901",
                    format = QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13,
                    displayDate = "Monday, April 15 2024 - 6:02:00 PM",
                    typeUI = HistoryTypeUI.GENERATE,
                    errorInGenerating = true,
                ),
            largeScreen = !phoneUI,
            navigationListeners = HistoryDetailNavigationListeners(),
            actionListeners = HistoryDetailActionListeners(),
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
        )
    }
}
// endregion screen previews
