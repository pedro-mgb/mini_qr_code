package com.pedroid.qrcodecompose.androidapp.home.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.GENERATE_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.SELECT_FORMAT_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.HISTORY_LIST_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HISTORY_DETAIL_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.SCAN_CAMERA_READER_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.SCAN_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.SETTINGS_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.ABOUT_APP_ROUTE
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.CONTACT_ROUTE

/**
 * enum containing items used in bottom navigation / navigation rail
 *
 */
sealed class HomeDestinationItem(
    @StringRes val itemTextId: Int,
    val itemIcon: ImageVector,
    val routeLabel: String,
    val encompassingRoutes: List<String> = listOf(routeLabel),
) {
    data object Scan : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_scan,
        itemIcon = Icons.Filled.ScanQRCode,
        routeLabel = SCAN_ROUTE,
        encompassingRoutes =
            listOf(
                SCAN_ROUTE,
                SCAN_CAMERA_READER_ROUTE,
            ),
    )

    data object Generate : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_generate,
        itemIcon = Icons.Filled.AddCircle,
        routeLabel = GENERATE_ROUTE,
        encompassingRoutes =
            listOf(
                GENERATE_ROUTE,
                SELECT_FORMAT_ROUTE,
            ),
    )

    data object History : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_history,
        itemIcon = Icons.Filled.DateRange,
        routeLabel = HISTORY_LIST_ROUTE,
        encompassingRoutes =
            listOf(
                HISTORY_LIST_ROUTE,
                HISTORY_DETAIL_ROUTE,
            ),
    )

    data object Settings : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_settings,
        itemIcon = Icons.Rounded.Settings,
        routeLabel = SETTINGS_ROUTE,
        encompassingRoutes =
            listOf(
                SETTINGS_ROUTE,
                CONTACT_ROUTE,
                ABOUT_APP_ROUTE,
            ),
    )

    companion object {
        val values: List<HomeDestinationItem> =
            listOf(
                Scan,
                Generate,
                History,
                Settings,
            )
    }
}

val defaultStartRoute: String = HomeDestinationItem.Scan.routeLabel
