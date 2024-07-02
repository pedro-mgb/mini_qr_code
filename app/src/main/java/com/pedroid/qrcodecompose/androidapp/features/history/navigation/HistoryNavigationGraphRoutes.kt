package com.pedroid.qrcodecompose.androidapp.features.history.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.pedroid.qrcodecompose.androidapp.features.expand.navigation.ExpandQRCodeNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.HistoryDetailNavigationListeners
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.historyDetailRoute
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.detail.navigateToHistoryDetail
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.expand.expandHistoryQRCodeRoute
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.expand.navigateToExpandHistoryQRCode

const val DELETED_HISTORY_ITEM_UID_KEY = "DELETED_HISTORY_ITEM_UID"

@ExperimentalSharedTransitionApi
fun NavGraphBuilder.historyFeatureNavigationRoutes(
    navController: NavController,
    largeScreen: Boolean = false,
    sharedTransitionScope: SharedTransitionScope,
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
                onExpand = {
                    navController.navigateToExpandHistoryQRCode(arguments = it)
                },
            ),
        largeScreen = largeScreen,
        sharedTransitionScope,
    )
    expandHistoryQRCodeRoute(
        navigationListeners =
            ExpandQRCodeNavigationListeners(
                goBack = {
                    navController.popBackStack()
                },
            ),
        sharedTransitionScope,
    )
}
