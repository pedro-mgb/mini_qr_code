package com.pedroid.qrcodecompose.androidapp.designsystem.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.MaterialTheme
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
        // contentColor = NavColorDefaults.navigationContentColor(),
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
        // contentColor = NavColorDefaults.navigationContentColor(),
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

// TODO employ this customization for colors; otherwise, if not needed, delete this
private object NavColorDefaults {
    @Composable
    fun navigationContentColor() = MaterialTheme.colorScheme.onSurfaceVariant

    @Composable
    fun navigationSelectedItemColor() = MaterialTheme.colorScheme.onPrimaryContainer

    @Composable
    fun navigationIndicatorColor() = MaterialTheme.colorScheme.primaryContainer
}