package com.pedroid.qrcodecompose.androidapp.home.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pedroid.qrcodecompose.androidapp.core.presentation.showPhoneUI
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppBackground
import com.pedroid.qrcodecompose.androidapp.home.navigation.QRCodeAppNavHost
import com.pedroid.qrcodecompose.androidapp.home.navigation.defaultStartRoute
import com.pedroid.qrcodecompose.androidapp.home.navigation.navigateToHomeDestinationItem

// region UI
@Composable
fun QRCodeApp(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController()
) {
    // A surface container using the 'background' color from the theme
    //  TODO fix issue with background being white initially, could be specific to system using light background
    QRAppBackground {
        Scaffold(
            bottomBar = {
                if (windowSizeClass.showPhoneUI()) {
                    BottomNavigationItems(
                        currentDestination = navController.currentDestination(),
                        onNavigateToHomeItem = navController::navigateToHomeDestinationItem
                    )
                }
            }
        ) { padding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (!windowSizeClass.showPhoneUI()) {
                    NavigationRailItems(
                        currentDestination = navController.currentDestination(),
                        onNavigateToHomeItem = navController::navigateToHomeDestinationItem
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
}
// endregion UI

// region utils
@Composable
fun NavHostController.currentDestination() =
    currentBackStackEntryAsState().value?.destination
// endregion utils
