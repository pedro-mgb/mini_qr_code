package com.pedroid.qrcodecompose.androidapp.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview

// region composables
@Composable
fun QRAppSnackbar(
    text: String,
    type: SnackbarType,
) {
    Snackbar(containerColor = type.backgroundColor()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = Dimens.spacingSmall),
                imageVector = type.icon,
                tint = type.contentColor(),
                contentDescription = null,
            )
            Text(text, color = type.contentColor())
        }
    }
}
// endregion composables

// region data
enum class SnackbarType(
    val backgroundColor: @Composable () -> Color,
    val contentColor: @Composable () -> Color,
    val icon: ImageVector,
) {
    INFO(
        backgroundColor = { MaterialTheme.colorScheme.inverseSurface },
        contentColor = { MaterialTheme.colorScheme.inverseOnSurface },
        icon = Icons.Filled.Info,
    ),
    SUCCESS(
        backgroundColor = { MaterialTheme.colorScheme.tertiaryContainer },
        contentColor = { MaterialTheme.colorScheme.onTertiaryContainer },
        icon = Icons.Filled.CheckCircle,
    ),
    ERROR(
        backgroundColor = { MaterialTheme.colorScheme.errorContainer },
        contentColor = { MaterialTheme.colorScheme.onErrorContainer },
        icon = Icons.Filled.Warning,
    ),
}
// endregion data

// region previews
@Preview
@Composable
fun QRAppSnackbarInfoPreview() {
    BaseQRCodeAppPreview {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
        ) {
            QRAppSnackbar(
                text = "This is an informative snackbar, not to be used for critical messages",
                type = SnackbarType.INFO,
            )
        }
    }
}

@Preview
@Composable
fun QRAppSnackbarSuccessPreview() {
    BaseQRCodeAppPreview {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
        ) {
            QRAppSnackbar(
                text = "This is a success snackbar, when an action the user takes is successful",
                type = SnackbarType.SUCCESS,
            )
        }
    }
}

@Preview
@Composable
fun QRAppSnackbarErrorPreview() {
    BaseQRCodeAppPreview {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
        ) {
            QRAppSnackbar(
                text =
                    "This is an error snackbar, when an action the user takes is NOT successful," +
                        " or just something going wrong in general",
                type = SnackbarType.ERROR,
            )
        }
    }
}
// endregion previews
