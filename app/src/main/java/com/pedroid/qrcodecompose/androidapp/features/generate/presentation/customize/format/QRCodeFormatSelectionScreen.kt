package com.pedroid.qrcodecompose.androidapp.features.generate.presentation.customize.format

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppToolbar
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.QRCodeSelectFormatListeners
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

// region composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRCodeFormatSelectionScreen(
    selectedFormat: QRCodeComposeXFormat,
    formatList: List<QRCodeComposeXFormat> = QRCodeComposeXFormat.entries.toList(),
    actionListeners: QRCodeSelectFormatListeners = QRCodeSelectFormatListeners(),
    largeScreen: Boolean = false,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QRAppToolbar(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.generate_code_select_format_title,
            onNavigationIconClick = actionListeners.onCancel,
        )
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.spacingMedium),
            text = stringResource(id = R.string.generate_code_select_format_description),
        )
        LazyColumn {
            items(formatList) {
                FormatItem(
                    data = it,
                    isSelected = selectedFormat == it,
                    clickListener = { actionListeners.onSelectFormat(it) },
                )
            }
        }
    }
}

@Composable
private fun FormatItem(
    data: QRCodeComposeXFormat,
    isSelected: Boolean,
    clickListener: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(if (isSelected) MaterialTheme.colorScheme.surfaceVariant else Color.Transparent)
                .clickable { clickListener() },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.spacingMedium),
        ) {
            Text(
                text = stringResource(data.titleStringId),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.size(Dimens.spacingExtraSmall))
            Text(
                text = stringResource(data.subtitleStringId),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

// endregion composables

// region previews
@PreviewScreenSizes
@Composable
fun QRCodeFormatSelectionScreenPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        QRCodeFormatSelectionScreen(
            selectedFormat = QRCodeComposeXFormat.AZTEC,
        )
    }
}
// endregion previews
