package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder

fun NavGraphBuilder.historyFeatureNavigationRoutes(navController: NavController) {
    historyListRoute(
        navigationListeners =
            HistoryListNavigationListeners(
                onSelectItem = {
                    // TODO show item details
                },
            ),
    )
}
