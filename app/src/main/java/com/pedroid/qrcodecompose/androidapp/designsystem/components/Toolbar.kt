package com.pedroid.qrcodecompose.androidapp.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pedroid.qrcodecompose.androidapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QRAppToolbar(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit,
    colors: TopAppBarColors = TopAppBarDefaults.centerAlignedTopAppBarColors(),
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.action_back_button),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
        },
        colors = colors,
        modifier = modifier.testTag("niaTopAppBar"),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun QRAppToolbarPreview() {
    QRAppToolbar(
        titleRes = android.R.string.untitled,
        onNavigationIconClick = { }
    )
}