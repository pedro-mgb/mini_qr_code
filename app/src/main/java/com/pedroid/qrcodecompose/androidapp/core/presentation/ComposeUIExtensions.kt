package com.pedroid.qrcodecompose.androidapp.core.presentation

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
@ReadOnlyComposable
fun stringResourceFromStringKey(stringKey: String): String {
    val context = LocalContext.current
    return context.getString(stringKey)
}

/**
 * Alternative to [androidx.compose.material3.adaptive.currentWindowAdaptiveInfo]
 *
 * The currentWindowAdaptiveInfo for some reason is not working properly with the @PreviewScreenSizes annotation
 * [androidx.compose.ui.tooling.preview.PreviewScreenSizes];
 * But with this method we can accurately get the window size class to display largeScreen content.
 * Since material3.adaptive is in alpha, perhaps the method will be fixed soon.
 */
@ExperimentalMaterial3WindowSizeClassApi
@Composable
@ReadOnlyComposable
fun getWindowSizeClassInPreview(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val size = DpSize(configuration.screenWidthDp.dp, configuration.screenHeightDp.dp)
    return WindowSizeClass.calculateFromSize(size)
}

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.ANNOTATION_CLASS,
    AnnotationTarget.FUNCTION,
)
@Preview(name = "en_locale", locale = "en")
@Preview(name = "pt_locale", locale = "pt")
annotation class PreviewAppLocales

@Composable
fun ComponentActivity.EdgeToEdgeEffect(darkTheme: Boolean) {
    // Update the edge to edge configuration to match the theme
    // This is the same parameters as the default enableEdgeToEdge call, but we manually
    // resolve whether or not to show dark theme using uiState, since it can be different
    // than the configuration's dark theme value based on the user preference.
    // credits to NIA app: https://github.com/android/nowinandroid/blob/main/app/src/main/kotlin/com/google/samples/apps/nowinandroid/MainActivity.kt
    // TODO implement a way to properly update current background color, depending on the content being shown
    DisposableEffect(darkTheme) {
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { darkTheme },
            navigationBarStyle =
                SystemBarStyle.auto(
                    android.graphics.Color.TRANSPARENT,
                    android.graphics.Color.TRANSPARENT,
                ) { darkTheme },
        )
        onDispose {}
    }
}

/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
