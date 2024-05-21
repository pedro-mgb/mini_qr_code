package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HistoryDetailNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.historyDetailRoute
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.navigateToHistoryDetail

const val DELETED_HISTORY_ITEM_UID_KEY = "DELETED_HISTORY_ITEM_UID"

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
                onUserDelete = {
                    navController.popBackStack()
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set(DELETED_HISTORY_ITEM_UID_KEY, it)
                },
                onGoBack = { navController.popBackStack() },
            ),
        largeScreen = largeScreen,
    )
}
