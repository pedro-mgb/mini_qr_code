package com.pedroid.qrcodecompose.androidapp.home.presentation

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppBottomNavBar
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppBottomNavBarItem
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppNavRail
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppNavRailItem
import com.pedroid.qrcodecompose.androidapp.home.navigation.HomeDestinationItem

// region UI
@Composable
fun BottomNavigationItems(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    onNavigateToHomeItem: (HomeDestinationItem) -> Unit,
    items: List<HomeDestinationItem> = HomeDestinationItem.values
) {
    QRAppBottomNavBar(modifier = modifier) {
        items.forEach { item ->
            val isSelected = currentDestination.isHomeItemInHierarchy(item)
            QRAppBottomNavBarItem(
                selected = isSelected,
                onClick = { onNavigateToHomeItem(item) },
                label = { item.NavText() },
                icon = { item.NavIcon() },
            )
        }
    }
}

@Composable
fun NavigationRailItems(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    onNavigateToHomeItem: (HomeDestinationItem) -> Unit,
    items: List<HomeDestinationItem> = HomeDestinationItem.values
) {
    QRAppNavRail(modifier = modifier) {
        items.forEach { item ->
            val isSelected = currentDestination.isHomeItemInHierarchy(item)
            QRAppNavRailItem(
                selected = isSelected,
                onClick = { onNavigateToHomeItem(item) },
                label = { item.NavText() },
                icon = { item.NavIcon() },
            )
        }
    }
}

@Composable
private fun HomeDestinationItem.NavIcon() {
    Icon(
        imageVector = itemIcon,
        contentDescription = null,
    )
}

@Composable
private fun HomeDestinationItem.NavText() {
    Text(text = stringResource(id = itemTextId))
}
// endregion UI

// region utils
private fun NavDestination?.isHomeItemInHierarchy(destination: HomeDestinationItem) =
    this?.hierarchy?.any {
        it.route?.contains(destination.routeLabel, ignoreCase = true) ?: false
    } ?: false
// endregion utils