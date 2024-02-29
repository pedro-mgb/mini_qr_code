package com.pedroid.qrcodecompose.androidapp.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview

@Composable
fun QRAppLargeTextBox(
    modifier: Modifier = Modifier,
    textValue: String,
    onTextChanged: (String) -> Unit,
    label: String = "",
    maxLines: Int = Int.MAX_VALUE,
    imeAction: ImeAction = ImeAction.Default,
) {
    OutlinedTextField(
        modifier = modifier,
        value = textValue,
        onValueChange = onTextChanged,
        label = {
            if (label.isNotBlank()) {
                Text(text = label)
            }
        },
        maxLines = maxLines,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
    )
}

@Preview
@Composable
fun LargeTextBoxPreview() {
    var text by remember { mutableStateOf("") }

    BaseQRCodeAppPreview {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QRAppLargeTextBox(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .padding(40.dp),
                textValue = text,
                onTextChanged = { text = it },
                imeAction = ImeAction.Done,
            )
        }
    }
}
