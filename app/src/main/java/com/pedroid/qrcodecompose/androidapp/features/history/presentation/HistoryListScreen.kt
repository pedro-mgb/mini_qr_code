package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppSelectableItem
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppSimpleToolbar
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.SelectAll
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.HistoryListActionListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.HistoryListNavigationListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlin.random.Random

// region screen composables
@Composable
fun HistoryListScreen(
    content: HistoryListContentState,
    selectionMode: HistorySelectionMode,
    navigationListeners: HistoryListNavigationListeners,
    actionListeners: HistoryListActionListeners,
) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize()) {
        if (selectionMode !is HistorySelectionMode.Selection) {
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
        } else {
            HistorySelectionToolbar(
                modifier = Modifier.fillMaxWidth(),
                actionListeners = actionListeners,
                selectedItemCount = selectionMode.currentlySelected,
            )
        }

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
                    selectionMode = selectionMode,
                    context = context,
                    actionListeners = actionListeners,
                    onClick = navigationListeners.onSelectItem,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistorySelectionToolbar(
    modifier: Modifier = Modifier,
    actionListeners: HistoryListActionListeners,
    selectedItemCount: Int,
) {
    QRAppSimpleToolbar(
        modifier = modifier,
        title =
            if (selectedItemCount > 1) {
                stringResource(id = R.string.history_item_selected_multiple, selectedItemCount)
            } else {
                stringResource(id = R.string.history_item_selected_single)
            },
        onNavigationIconClick = actionListeners.onSelectionBackPressed,
        actions = {
            IconButton(onClick = actionListeners.onSelectAllItems) {
                Icon(
                    imageVector = Icons.Outlined.SelectAll,
                    contentDescription = stringResource(id = R.string.history_item_select_all_action),
                )
            }
            IconButton(onClick = { actionListeners.onDeletePressed(selectedItemCount) }) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    tint = MaterialTheme.colorScheme.secondary,
                    contentDescription = stringResource(id = R.string.history_delete_action),
                )
            }
        },
    )
}

@Composable
private fun HistoryDataList(
    modifier: Modifier = Modifier,
    content: HistoryListContentState.DataList,
    selectionMode: HistorySelectionMode,
    context: Context,
    actionListeners: HistoryListActionListeners,
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
                    QRAppSelectableItem(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    color =
                                        if (item.selected) {
                                            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                                        } else {
                                            Color.Transparent
                                        },
                                ),
                        inSelectableMode = selectionMode is HistorySelectionMode.Selection,
                        isSelected = item.selected,
                        checkboxPadding =
                            PaddingValues(
                                start = Dimens.spacingSmall,
                                top = Dimens.spacingSmall,
                                bottom = Dimens.spacingSmall,
                                end = Dimens.spacingNone,
                            ),
                        onSelectionChange = {
                            actionListeners.onItemSelectedToggle(item.uid)
                        },
                        regularClickListener = {
                            onClick(item.uid)
                        },
                    ) {
                        HistoryListDataItem(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = Dimens.spacingMedium,
                                        vertical = Dimens.spacingSmall,
                                    ),
                            item,
                        )
                    }
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
                textAlign = TextAlign.End,
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

@Composable
private fun HistoryListDataItem(
    modifier: Modifier = Modifier,
    item: HistoryListItem.Data,
) {
    Card(modifier = modifier) {
        ConstraintLayout(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.spacingSmall),
        ) {
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
                            width = Dimension.fillToConstraints
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
                    Modifier
                        .fillMaxWidth()
                        .constrainAs(value) {
                            width = Dimension.fillToConstraints
                            linkTo(start = icon.end, end = barrier, bias = 0f)
                            linkTo(
                                top = scanOrGenerateText.bottom,
                                bottom = parent.bottom,
                                topMargin = 2.dp,
                            )
                        }
                        .padding(horizontal = Dimens.spacingSmall),
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
                text = item.displayDate,
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
            selectionMode = HistorySelectionMode.None,
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
            content = HistoryListContentState.DataList(mockHistoryDataList(selectSomeItems = false)),
            selectionMode = HistorySelectionMode.None,
            navigationListeners = HistoryListNavigationListeners(),
            actionListeners = HistoryListActionListeners(),
        )
    }
}

@PreviewScreenSizes
@Composable
fun HistoryListScreenSelectingPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        HistoryListScreen(
            content = HistoryListContentState.DataList(mockHistoryDataList(selectSomeItems = true)),
            selectionMode = HistorySelectionMode.Selection(5),
            navigationListeners = HistoryListNavigationListeners(),
            actionListeners = HistoryListActionListeners(),
        )
    }
}

@Composable
private fun mockHistoryDataList(selectSomeItems: Boolean): List<HistoryListItem> =
    listOf(
        HistoryListItem.SectionHeader(stringResource(id = R.string.history_header_today)),
        mockHistoryDataItem(selected = selectSomeItems),
        HistoryListItem.SectionHeader(stringResource(id = R.string.history_header_yesterday)),
        mockHistoryDataItem("a very long text with new line, should eclipsize\nthis text should not be shown"),
        mockHistoryDataItem(typeUI = HistoryTypeUI.GENERATE, format = QRCodeComposeXFormat.BARCODE_128),
        HistoryListItem.SectionHeader("A year ago..."),
        mockHistoryDataItem(typeUI = HistoryTypeUI.SCAN_FILE),
        mockHistoryDataItem(selected = selectSomeItems),
        mockHistoryDataItem(selected = selectSomeItems),
        mockHistoryDataItem(selected = selectSomeItems),
        mockHistoryDataItem(selected = selectSomeItems),
        mockHistoryDataItem(selected = selectSomeItems),
        mockHistoryDataItem(selected = selectSomeItems),
        mockHistoryDataItem(selected = selectSomeItems),
    )

private fun mockHistoryDataItem(
    dataValue: String = "some value",
    date: String = "a formatted\ndate 2 lines",
    typeUI: HistoryTypeUI = HistoryTypeUI.SCAN_CAMERA,
    format: QRCodeComposeXFormat = QRCodeComposeXFormat.QR_CODE,
    selected: Boolean = false,
): HistoryListItem.Data =
    HistoryListItem.Data(
        uid = Random.nextLong(),
        value = dataValue,
        displayDate = date,
        typeUI = typeUI,
        formatStringId = format.titleStringId,
        selected = selected,
    )
// endregion screen previews
