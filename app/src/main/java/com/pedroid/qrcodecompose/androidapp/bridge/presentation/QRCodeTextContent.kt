package com.pedroid.qrcodecompose.androidapp.bridge.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens

@Composable
fun QRCodeTextContent(
    modifier: Modifier = Modifier,
    qrCode: String,
    textAlign: TextAlign = TextAlign.Start,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border =
            BorderStroke(
                width = Dimens.borderWidthSmall,
                color = MaterialTheme.colorScheme.onBackground,
            ),
    ) {
        SelectionContainer {
            Text(
                modifier =
                    Modifier
                        .padding(Dimens.spacingMedium)
                        .verticalScroll(rememberScrollState()),
                text = qrCode,
                textAlign = textAlign,
            )
        }
    }
}
