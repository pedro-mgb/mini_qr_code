package com.pedroid.qrcodecompose.androidapp.designsystem.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview

// region composables
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QRAppSelectableItem(
    modifier: Modifier = Modifier,
    inSelectableMode: Boolean,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    regularClickListener: () -> Unit = {},
    longClickAccessibilityLabel: String? = null,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier =
            modifier.combinedClickable(
                onLongClickLabel = longClickAccessibilityLabel,
                onLongClick = {
                    if (inSelectableMode) {
                        onSelectionChange(!isSelected)
                    } else {
                        onSelectionChange(true)
                    }
                },
                onClick = {
                    if (inSelectableMode) {
                        onSelectionChange(!isSelected)
                    } else {
                        regularClickListener()
                    }
                },
            ),
        verticalAlignment = verticalAlignment,
    ) {
        if (inSelectableMode) {
            Checkbox(
                modifier = Modifier.padding(Dimens.spacingSmall),
                checked = isSelected,
                onCheckedChange = onSelectionChange,
            )
        }
        this.content()
    }
}
// endregion composables

// region previews
@Preview
@Composable
fun QRAppFunctionalSelectableItemsPreview() {
    var itemList by remember {
        mutableStateOf(
            mutableListOf<Pair<String, Boolean>>().apply {
                repeat(40) {
                    add(Pair("Text $it", false))
                }
            }.toList(),
        )
    }
    val inSelectableMode by remember {
        derivedStateOf {
            itemList.any { it.second }
        }
    }
    BaseQRCodeAppPreview {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(itemList) { index, item ->
                QRAppSelectableItem(
                    inSelectableMode = inSelectableMode,
                    isSelected = item.second,
                    onSelectionChange = { selected ->
                        itemList =
                            itemList.mapIndexed { mapIdx, mapItem ->
                                if (mapIdx == index) {
                                    mapItem.copy(second = selected)
                                } else {
                                    mapItem
                                }
                            }
                    },
                ) {
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = Dimens.spacingLarge, bottom = Dimens.spacingLarge),
                        text = item.first,
                    )
                }
            }
        }
    }
}
// endregion previews
