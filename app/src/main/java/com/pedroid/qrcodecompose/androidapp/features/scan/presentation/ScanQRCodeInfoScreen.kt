package com.pedroid.qrcodecompose.androidapp.features.scan.presentation

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
import androidx.compose.ui.unit.dp
import com.pedroid.qrcodecompose.androidapp.R

@Composable
fun ScanQRCodeInfoScreen() {
    // TODO implement feature
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(id = R.string.bottom_navigation_item_scan))
        Spacer(modifier = Modifier.height(20.dp))
        Text(stringResource(id = R.string.to_be_implemented))
    }
}