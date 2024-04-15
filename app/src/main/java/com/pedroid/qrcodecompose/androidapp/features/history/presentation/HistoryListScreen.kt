package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.getString
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.HistoryListActionListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.HistoryListNavigationListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlin.random.Random

// region screen composables
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HistoryListScreen(
    content: HistoryListContentState,
    navigationListeners: HistoryListNavigationListeners,
    actionListeners: HistoryListActionListeners,
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        HistoryInfoTopHeader(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = Dimens.spacingLarge,
                        end = Dimens.spacingLarge,
                        top = Dimens.spacingLarge,
                        bottom = Dimens.spacingSmall,
                    ),
            moreInformationClicked = actionListeners.onMoreInfoRequested,
        )

        when (content) {
            HistoryListContentState.Idle -> {
                // no UI content to add
            }
            HistoryListContentState.Empty -> {
                HistoryEmptyContent(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = Dimens.spacingExtraLarge),
                )
            }
            is HistoryListContentState.DataList -> {
                HistoryDataList(
                    modifier = Modifier.fillMaxSize(),
                    content = content,
                    context = context,
                    onClick = navigationListeners.onSelectItem,
                )
            }
        }
    }
}

@Composable
@ExperimentalComposeUiApi
private fun HistoryDataList(
    modifier: Modifier = Modifier,
    content: HistoryListContentState.DataList,
    context: Context,
    onClick: (Long) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = Dimens.spacingMedium),
    ) {
        items(
            content.list,
            key = {
                if (it is HistoryListItem.Data) it.uid.toString() else it.toString()
            },
        ) { item ->
            when (item) {
                is HistoryListItem.SectionHeader ->
                    HistorySectionHeader(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = Dimens.spacingMedium,
                                    end = Dimens.spacingMedium,
                                    top = Dimens.spacingLarge,
                                    bottom = Dimens.spacingSmall,
                                ),
                        item = item,
                        context = context,
                    )
                is HistoryListItem.Data ->
                    HistoryListDataItem(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable { onClick(item.uid) }
                                .padding(horizontal = Dimens.spacingMedium, vertical = Dimens.spacingSmall),
                        item,
                    )
            }
        }
    }
}

@Composable
private fun HistoryInfoTopHeader(
    modifier: Modifier = Modifier,
    moreInformationClicked: () -> Unit,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(id = R.string.history_top_header_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        TextButton(onClick = moreInformationClicked) {
            Text(
                modifier = Modifier.padding(Dimens.spacingExtraSmall),
                text = stringResource(id = R.string.history_top_header_more_information_button),
            )
        }
    }
}

@Composable
private fun HistoryEmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(modifier = Modifier.padding(Dimens.spacingSmall), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.size(150.dp),
                imageVector = Icons.Filled.DateRange,
                contentDescription = null,
            )
            Column {
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.ScanQRCode,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.padding(Dimens.spacingExtraSmall))
                Icon(
                    modifier = Modifier.size(40.dp),
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = null,
                )
            }
        }
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.spacingMedium),
            text = stringResource(id = R.string.history_empty_list),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HistorySectionHeader(
    modifier: Modifier = Modifier,
    item: HistoryListItem.SectionHeader,
    context: Context,
) {
    Text(
        modifier = modifier,
        text = context.getString(item.text),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.titleLarge,
    )
}

@ExperimentalComposeUiApi
@Composable
private fun HistoryListDataItem(
    modifier: Modifier = Modifier,
    item: HistoryListItem.Data,
) {
    Card(modifier = modifier) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth().padding(Dimens.spacingSmall)) {
            val (icon, scanOrGenerateText, value, format, dateTime) = createRefs()
            val barrier = createStartBarrier(format, dateTime)
            Icon(
                modifier =
                    Modifier.constrainAs(icon) {
                        height = Dimension.fillToConstraints
                        width = Dimension.value(40.dp)
                        start.linkTo(parent.start)
                        linkTo(top = parent.top, bottom = parent.bottom)
                    },
                imageVector = item.typeUI.icon,
                tint = item.typeUI.color(),
                contentDescription = null,
            )

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(scanOrGenerateText) {
                            linkTo(start = icon.end, end = barrier, bias = 0f)
                            linkTo(top = parent.top, bottom = value.top, bottomMargin = 2.dp)
                        }
                        .padding(horizontal = Dimens.spacingSmall),
                text = stringResource(id = item.typeUI.typeStringId),
                color = item.typeUI.color(),
                maxLines = 1,
            )
            Text(
                modifier =
                    Modifier.fillMaxWidth().constrainAs(value) {
                        width = Dimension.fillToConstraints
                        linkTo(start = icon.end, end = barrier, bias = 0f)
                        linkTo(top = scanOrGenerateText.bottom, bottom = parent.bottom, topMargin = 2.dp)
                    }.padding(horizontal = Dimens.spacingSmall),
                text = item.value,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier =
                    Modifier.constrainAs(format) {
                        end.linkTo(parent.end)
                        linkTo(top = parent.top, bottom = dateTime.top)
                    },
                text = stringResource(id = item.formatStringId),
                color = item.typeUI.color(),
                style = MaterialTheme.typography.labelMedium,
                maxLines = 1,
            )
            Text(
                modifier =
                    Modifier
                        .constrainAs(dateTime) {
                            end.linkTo(parent.end)
                            linkTo(top = format.bottom, bottom = parent.bottom)
                        },
                text = item.formattedDate,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.End,
            )
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewScreenSizes
@Composable
fun HistoryListScreenEmptyPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        HistoryListScreen(
            content = HistoryListContentState.Empty,
            navigationListeners = HistoryListNavigationListeners(),
            actionListeners = HistoryListActionListeners(),
        )
    }
}

@PreviewScreenSizes
@Composable
fun HistoryListScreenWithContentPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        HistoryListScreen(
            content = HistoryListContentState.DataList(mockHistoryDataList()),
            navigationListeners = HistoryListNavigationListeners(),
            actionListeners = HistoryListActionListeners(),
        )
    }
}

@Composable
private fun mockHistoryDataList(): List<HistoryListItem> =
    listOf(
        HistoryListItem.SectionHeader(stringResource(id = R.string.history_header_today)),
        mockHistoryDataItem(),
        HistoryListItem.SectionHeader(stringResource(id = R.string.history_header_yesterday)),
        mockHistoryDataItem("a very long text with new line, should eclipsize\nthis text should not be shown"),
        mockHistoryDataItem(typeUI = HistoryTypeUI.GENERATE, format = QRCodeComposeXFormat.BARCODE_128),
        HistoryListItem.SectionHeader("A year ago..."),
        mockHistoryDataItem(typeUI = HistoryTypeUI.SCAN_FILE),
        mockHistoryDataItem(),
        mockHistoryDataItem(),
        mockHistoryDataItem(),
        mockHistoryDataItem(),
        mockHistoryDataItem(),
        mockHistoryDataItem(),
        mockHistoryDataItem(),
    )

private fun mockHistoryDataItem(
    dataValue: String = "some value",
    date: String = "a formatted\ndate 2 lines",
    typeUI: HistoryTypeUI = HistoryTypeUI.SCAN_CAMERA,
    format: QRCodeComposeXFormat = QRCodeComposeXFormat.QR_CODE,
): HistoryListItem.Data =
    HistoryListItem.Data(
        uid = Random.nextLong(),
        value = dataValue,
        formattedDate = date,
        typeUI = typeUI,
        formatStringId = format.titleStringId,
    )
// endregion screen previews
