package com.pedroid.qrcodecompose.androidapp.features.history.presentation.delete

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun deleteButtonColors(): ButtonColors =
    ButtonDefaults.buttonColors().copy(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
    )
