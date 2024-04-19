package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HistoryDetailNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.historyDetailRoute
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.navigateToHistoryDetail

fun NavGraphBuilder.historyFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean = false,
) {
    historyListRoute(
        navigationListeners =
            HistoryListNavigationListeners(
                onSelectItem = {
                    navController.navigateToHistoryDetail(uid = it)
                },
            ),
    )
    historyDetailRoute(
        navigationListeners =
            HistoryDetailNavigationListeners(
                onGoBack = { navController.popBackStack() },
            ),
        largeScreen = largeScreen,
    )
}
