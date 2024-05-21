package com.pedroid.qrcodecompose.androidapp.features.history.presentation.delete

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview

// region screen composables
@Composable
fun HistoryDeleteConfirmationDialog(
    title: String,
    description: String,
    deleteActionButton: String,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier =
                Modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .fillMaxWidth(fraction = 0.8f),
            shape = RoundedCornerShape(Dimens.roundedCornerMedium),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(Dimens.spacingLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = Dimens.spacingMedium),
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                )
                Text(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = 0.8f)
                            .padding(bottom = Dimens.spacingExtraLarge),
                    text = description,
                )

                Button(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = 0.8f)
                            .padding(bottom = Dimens.spacingSmall),
                    colors = deleteButtonColors(),
                    onClick = onDelete,
                ) {
                    Text(deleteActionButton)
                }
                OutlinedButton(
                    modifier =
                        Modifier
                            .fillMaxWidth(fraction = 0.8f)
                            .padding(bottom = Dimens.spacingMedium),
                    onClick = onDismiss,
                ) {
                    Text(stringResource(id = R.string.action_cancel))
                }
            }
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewScreenSizes
@Composable
fun HistoryDeleteConfirmationDialogPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        HistoryDeleteConfirmationDialog(
            title = "Delete history",
            description = "Are you sure you want to delete the history?\nNot reversible",
            deleteActionButton = "Delete",
            onDelete = {},
            onDismiss = {},
        )
    }
}
// endregion screen composables
