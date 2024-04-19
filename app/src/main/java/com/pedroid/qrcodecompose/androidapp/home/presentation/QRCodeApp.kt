package com.pedroid.qrcodecompose.androidapp.home.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pedroid.qrcodecompose.androidapp.core.presentation.LocalSnackbarHostState
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.QRAppSnackbarHost
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppBackground
import com.pedroid.qrcodecompose.androidapp.home.navigation.QRCodeAppNavHost
import com.pedroid.qrcodecompose.androidapp.home.navigation.defaultStartRoute
import com.pedroid.qrcodecompose.androidapp.home.navigation.navigateToHomeDestinationItem

// region UI
@Composable
fun QRCodeApp(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    // A surface container using the 'background' color from the theme
    QRAppBackground(modifier = Modifier.fillMaxSize()) {
        val snackbarHostState = remember { SnackbarHostState() }
        CompositionLocalProvider(
            LocalSnackbarHostState provides snackbarHostState,
        ) {
            QRCodeAppFrame(
                windowSizeClass = windowSizeClass,
                snackbarHostState = snackbarHostState,
                navController = navController,
            )
        }
    }
}

@Composable
private fun QRCodeAppFrame(
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
) {
    Scaffold(
        bottomBar = {
            if (windowSizeClass.showPhoneUI()) {
                BottomNavigationItems(
                    currentDestination = navController.currentDestination(),
                    onNavigateToHomeItem = navController::navigateToHomeDestinationItem,
                )
            }
        },
        snackbarHost = {
            QRAppSnackbarHost(snackbarHostState)
        },
    ) { padding ->
        Row(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            if (!windowSizeClass.showPhoneUI()) {
                NavigationRailItems(
                    currentDestination = navController.currentDestination(),
                    onNavigateToHomeItem = navController::navigateToHomeDestinationItem,
                )
            }

            QRCodeAppNavHost(
                navHostController = navController,
                startDestination = defaultStartRoute,
                windowWidthSizeClass = windowSizeClass.widthSizeClass,
            )
        }
    }
}

// endregion UI

// region utils
@Composable
fun NavHostController.currentDestination() = currentBackStackEntryAsState().value?.destination
// endregion utils
