package com.pedroid.qrcodecompose.androidapp.designsystem.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.MaterialTheme.colorScheme as cs

private val darkColorScheme =
    darkColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_onSecondary,
        secondaryContainer = md_theme_dark_secondaryContainer,
        onSecondaryContainer = md_theme_dark_onSecondaryContainer,
        tertiary = md_theme_dark_tertiary,
        onTertiary = md_theme_dark_onTertiary,
        tertiaryContainer = md_theme_dark_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_onTertiaryContainer,
        error = md_theme_dark_error,
        errorContainer = md_theme_dark_errorContainer,
        onError = md_theme_dark_onError,
        onErrorContainer = md_theme_dark_onErrorContainer,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_onSurface,
        surfaceVariant = md_theme_dark_surfaceVariant,
        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
        outline = md_theme_dark_outline,
        inverseOnSurface = md_theme_dark_inverseOnSurface,
        inverseSurface = md_theme_dark_inverseSurface,
        inversePrimary = md_theme_dark_inversePrimary,
        surfaceTint = md_theme_dark_surfaceTint,
        outlineVariant = md_theme_dark_outlineVariant,
        scrim = md_theme_dark_scrim,
    )

@Composable
fun QRCodeComposeCameraXTheme(
    // Dynamic color is available on Android 12+; for now only dark theme available without dynamic color
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                dynamicDarkColorScheme(context)
            }

            else -> darkColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}

// currently these extension colors are hardcoded, if the app ever has light + dark theme, these should be made such
//  that the colors are retrieved via theme (e.g. saving the extra colors via composition local
val ColorScheme.success: Color
    get() = dark_Success
val ColorScheme.onSuccess: Color
    get() = dark_onSuccess
val ColorScheme.successContainer: Color
    get() = dark_SuccessContainer
val ColorScheme.onSuccessContainer: Color
    get() = dark_onSuccessContainer

@Preview
@Composable
fun ThemeColorsPreview() {
    @Composable
    fun ColorItem(
        text: String,
        color: Color,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            // Magenta to have ok contrast with both light and dark background
            Text(
                modifier = Modifier.width(200.dp),
                text = text,
                color = Color.Magenta,
            )
            Box(
                modifier =
                    Modifier
                        .padding(end = 40.dp, top = 4.dp, bottom = 4.dp)
                        .width(100.dp)
                        .height(25.dp)
                        .background(color),
            ) { }
        }
    }

    QRCodeComposeCameraXTheme {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
        ) {
            ColorItem(text = "Primary", color = cs.primary)
            ColorItem(text = "On Primary", color = cs.onPrimary)
            ColorItem(text = "Primary Container", color = cs.primaryContainer)
            ColorItem(text = "On Primary Container", color = cs.primary)
            ColorItem(text = "Secondary", color = cs.secondary)
            ColorItem(text = "On Secondary", color = cs.onSecondary)
            ColorItem(text = "Secondary Container", color = cs.secondaryContainer)
            ColorItem(text = "On Secondary Container", color = cs.onSecondaryContainer)
            ColorItem(text = "Tertiary", color = cs.tertiary)
            ColorItem(text = "On Tertiary", color = cs.onTertiary)
            ColorItem(text = "Tertiary Container", color = cs.tertiaryContainer)
            ColorItem(text = "On Tertiary Container", color = cs.onTertiaryContainer)
            ColorItem(text = "Error", color = cs.error)
            ColorItem(text = "Error Container", color = cs.errorContainer)
            ColorItem(text = "On Error", color = cs.onError)
            ColorItem(text = "On Error Container", color = cs.onErrorContainer)
            ColorItem(text = "Background", color = cs.background)
            ColorItem(text = "On Background", color = cs.onBackground)
            ColorItem(text = "Surface", color = cs.surface)
            ColorItem(text = "On Surface", color = cs.onSurface)
            ColorItem(text = "Surface Variant", color = cs.surfaceVariant)
            ColorItem(text = "On Surface Variant", color = cs.onSurfaceVariant)
            ColorItem(text = "Outline", color = cs.outline)
            ColorItem(text = "Inverse On Surface", color = cs.inverseOnSurface)
            ColorItem(text = "Inverse Surface", color = cs.inverseSurface)
            ColorItem(text = "Inverse Primary", color = cs.inversePrimary)
            ColorItem(text = "Surface Tint", color = cs.surfaceTint)
            ColorItem(text = "Outline Variant", color = cs.outlineVariant)
            ColorItem(text = "Scrim", color = cs.scrim)
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            ColorItem(text = "Success", color = cs.success)
            ColorItem(text = "On Success", color = cs.onSuccess)
            ColorItem(text = "Success Container", color = cs.successContainer)
            ColorItem(text = "On Success Container", color = cs.onSuccessContainer)
        }
    }
}
