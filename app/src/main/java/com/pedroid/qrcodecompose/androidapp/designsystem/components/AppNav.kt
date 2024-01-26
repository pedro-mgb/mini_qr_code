package com.pedroid.qrcodecompose.androidapp.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QRAppBottomNavBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(
        modifier = modifier,
        content = content,
    )
}

@Composable
fun RowScope.QRAppBottomNavBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable (() -> Unit),
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        label = label,
        icon = icon,
        modifier = modifier
    )
}

@Composable
fun QRAppNavRail(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    NavigationRail(
        modifier = modifier,
        content = content,
    )
}

@Composable
fun QRAppNavRailItem(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable (() -> Unit),
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationRailItem(
        selected = selected,
        onClick = onClick,
        label = label,
        icon = icon,
        modifier = modifier
    )
}
