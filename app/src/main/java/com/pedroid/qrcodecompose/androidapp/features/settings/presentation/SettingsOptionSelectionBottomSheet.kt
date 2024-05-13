package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.stringResourceFromStringKey
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// region screen composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsOptionSelectionBottomSheet(
    option: SettingsMainContentItem.OptionWithActionSelection,
    actionListener: (SettingsMainUIAction) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    fun CoroutineScope.dismissBottomSheet(dismissListener: () -> Unit = onDismiss) =
        launch {
            sheetState.hide()
        }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                dismissListener()
            }
        }

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        SettingsOptionSelectionBottomSheetUI(
            optionContent = option,
            actionListener = {
                scope.dismissBottomSheet {
                    onDismiss()
                    actionListener(it)
                }
            },
            dismiss = {
                scope.dismissBottomSheet()
            },
        )
    }
}

@Composable
private fun SettingsOptionSelectionBottomSheetUI(
    optionContent: SettingsMainContentItem.OptionWithActionSelection,
    actionListener: (SettingsMainUIAction) -> Unit,
    dismiss: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(vertical = Dimens.spacingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.spacingMedium, start = Dimens.spacingExtraLarge, end = Dimens.spacingExtraLarge),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(Dimens.iconButtonSize),
                imageVector = optionContent.startIcon,
                contentDescription = null,
            )
            Text(
                modifier = Modifier.padding(Dimens.spacingMedium),
                text = stringResourceFromStringKey(stringKey = optionContent.text),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
            )
        }

        optionContent.selectionContent.options.forEachIndexed { index, titleAndDescription ->
            val optionSelected = titleAndDescription.title == optionContent.currentOption
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(if (optionSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                        .clickable {
                            actionListener(optionContent.selectionContent.onSelect(index))
                        }
                        .padding(Dimens.spacingMedium),
            ) {
                Text(
                    text = stringResourceFromStringKey(titleAndDescription.title),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(modifier = Modifier.size(Dimens.spacingExtraSmall))
                if (titleAndDescription.description.isNotEmpty()) {
                    Text(
                        text = stringResourceFromStringKey(titleAndDescription.description),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        OutlinedButton(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.6f)
                    .padding(vertical = Dimens.spacingMedium),
            onClick = dismiss,
        ) {
            Text(stringResource(id = R.string.action_cancel))
        }
    }
}
// endregion screen composables
