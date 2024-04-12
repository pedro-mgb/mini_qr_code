package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens

// region screen composables
@Composable
fun HistoryListScreen(content: List<HistoryListItem>) {
    // TODO implement feature
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(id = R.string.bottom_navigation_item_history))
        Spacer(modifier = Modifier.height(Dimens.spacingMedium))
        Text(stringResource(id = R.string.to_be_implemented))
    }
}

@Composable
private fun HistoryListDataItem(
    modifier: Modifier = Modifier,
    item: HistoryListItem.Data,
    onClick: (Long) -> Unit,
) {
}
// endregion screen composables

// region screen previews
@Preview
@Composable
fun HistoryListScreenEmptyPreview() {
}

@PreviewScreenSizes
@Composable
fun HistoryListScreenWithContentPreview() {
}
// endregion screen previews
