package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import kotlinx.coroutines.launch

// region screen composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryMoreInfoBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        modifier =
            Modifier
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                .fillMaxWidth(),
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        HistoryMoreInfoBottomSheetUI(
            onCloseClicked = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            },
        )
    }
}

@Composable
private fun HistoryMoreInfoBottomSheetUI(onCloseClicked: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(Dimens.spacingExtraLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.spacingMedium),
            text = stringResource(id = R.string.history_top_header_more_information_button),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
        )

        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = Dimens.spacingExtraLarge),
            text = stringResource(id = R.string.history_more_information_description),
        )

        Button(
            modifier =
                Modifier
                    .fillMaxWidth(fraction = 0.6f)
                    .padding(bottom = Dimens.spacingMedium),
            onClick = onCloseClicked,
        ) {
            Text(stringResource(id = R.string.action_close))
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewScreenSizes
@Composable
fun HistoryMoreInfoBottomSheetUIPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
        ) {
            HistoryMoreInfoBottomSheetUI {
                // nothing to do on close
            }
        }
    }
}
// endregion screen composables
