package com.pedroid.qrcodecompose.androidapp.home.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.features.generate.navigation.generateFeatureNavigationRoutes
import com.pedroid.qrcodecompose.androidapp.features.history.navigation.historyFeatureNavigationRoutes
import com.pedroid.qrcodecompose.androidapp.features.scan.navigation.scanningFeatureNavigationRoutes
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.settingsFeatureNavigationRoutes
import kotlin.reflect.KClass

@ExperimentalSharedTransitionApi
@Composable
fun QRCodeAppNavHost(
    navHostController: NavHostController,
    windowWidthSizeClass: WindowWidthSizeClass,
    modifier: Modifier = Modifier,
    startDestination: KClass<*>,
    sharedTransitionScope: SharedTransitionScope,
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        scanningFeatureNavigationRoutes(
            navHostController,
            largeScreen = !windowWidthSizeClass.showPhoneUI(),
            sharedTransitionScope,
        )
        generateFeatureNavigationRoutes(
            navHostController,
            largeScreen = !windowWidthSizeClass.showPhoneUI(),
            sharedTransitionScope,
        )
        historyFeatureNavigationRoutes(
            navHostController,
            largeScreen = !windowWidthSizeClass.showPhoneUI(),
            sharedTransitionScope,
        )
        settingsFeatureNavigationRoutes(navHostController)
    }
}
