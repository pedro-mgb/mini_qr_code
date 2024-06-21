package com.pedroid.qrcodecompose.androidapp.home.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ScanQRCode
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.GenerateQRCodeHomeRoute
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.customize.format.SelectFormatRoute
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.expand.ExpandGeneratedQRCodeRoute
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.HistoryListRoute
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HistoryDetailRoute
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.ScanCameraReaderRoute
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.ScanQRCodeInfoRoute
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.fromfile.ScanFileReaderRoute
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.SettingMainRoute
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.AboutAppRoute
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.ContactRoute
import kotlin.reflect.KClass

/**
 * enum containing items used in bottom navigation / navigation rail
 *
 */
sealed class HomeDestinationItem(
    @StringRes val itemTextId: Int,
    val itemIcon: ImageVector,
    val routeClass: KClass<*>,
    val encompassingRoutes: List<KClass<*>> = listOf(routeClass),
) {
    data object Scan : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_scan,
        itemIcon = Icons.Filled.ScanQRCode,
        routeClass = ScanQRCodeInfoRoute::class,
        encompassingRoutes =
            listOf(
                ScanQRCodeInfoRoute::class,
                ScanCameraReaderRoute::class,
                ScanFileReaderRoute::class,
            ),
    )

    data object Generate : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_generate,
        itemIcon = Icons.Filled.AddCircle,
        routeClass = GenerateQRCodeHomeRoute::class,
        encompassingRoutes =
            listOf(
                GenerateQRCodeHomeRoute::class,
                SelectFormatRoute::class,
                ExpandGeneratedQRCodeRoute::class,
            ),
    )

    data object History : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_history,
        itemIcon = Icons.Filled.DateRange,
        routeClass = HistoryListRoute::class,
        encompassingRoutes =
            listOf(
                HistoryListRoute::class,
                HistoryDetailRoute::class,
            ),
    )

    data object Settings : HomeDestinationItem(
        itemTextId = R.string.bottom_navigation_item_settings,
        itemIcon = Icons.Rounded.Settings,
        routeClass = SettingMainRoute::class,
        encompassingRoutes =
            listOf(
                SettingMainRoute::class,
                ContactRoute::class,
                AboutAppRoute::class,
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

val defaultStartRoute: KClass<*> = HomeDestinationItem.Scan.routeClass

fun HomeDestinationItem.encompassesRoute(route: String?): Boolean =
    route?.let { r ->
        encompassingRoutes.map { it.simpleName ?: "" }.any {
            r.contains(it, ignoreCase = true)
        }
    } ?: false
