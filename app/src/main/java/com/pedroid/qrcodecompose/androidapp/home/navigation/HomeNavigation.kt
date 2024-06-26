package com.pedroid.qrcodecompose.androidapp.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.navigateToGenerateQRCodeInfo
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.navigateToQRCodeHistoryList
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.navigateToScanQRCodeInfo
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.navigateToMainSettings

fun NavController.navigateToHomeDestinationItem(item: HomeDestinationItem) {
    if (item.encompassesDestination(currentBackStackEntry?.destination)) {
        // already in the current home destination, no need to navigate
        return
    }

    val topLevelDestinationsOptions = buildNavOptions()

    when (item) {
        is HomeDestinationItem.Scan -> navigateToScanQRCodeInfo(topLevelDestinationsOptions)
        is HomeDestinationItem.Generate -> navigateToGenerateQRCodeInfo(topLevelDestinationsOptions)
        is HomeDestinationItem.History -> navigateToQRCodeHistoryList(topLevelDestinationsOptions)
        is HomeDestinationItem.Settings -> navigateToMainSettings(topLevelDestinationsOptions)
    }
}

private fun NavController.buildNavOptions(): NavOptions =
    navOptions {
        // Pop up to the start destination of the graph to
        //  avoid building up a large stack of destinations
        //  on the back stack as users select items
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        //  re-selecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }
