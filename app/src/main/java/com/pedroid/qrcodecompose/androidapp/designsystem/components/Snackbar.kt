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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.onSuccessContainer
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.successContainer
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview

// region composables
@Composable
fun QRAppSnackbar(visuals: QRAppSnackbarVisuals) {
    Snackbar(
        modifier = Modifier.padding(Dimens.spacingLarge),
        containerColor = visuals.type.backgroundColor(),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = Dimens.spacingSmall),
                imageVector = visuals.type.icon,
                tint = visuals.type.contentColor(),
                contentDescription = null,
            )
            Text(visuals.message, color = visuals.type.contentColor())
        }
    }
}
// endregion composables

// region data
data class QRAppSnackbarVisuals(
    val type: SnackbarType,
    override val message: String,
) : SnackbarVisuals {
    override val actionLabel: String? = null
    override val duration: SnackbarDuration = SnackbarDuration.Short
    override val withDismissAction: Boolean = false
}

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
        backgroundColor = { MaterialTheme.colorScheme.successContainer },
        contentColor = { MaterialTheme.colorScheme.onSuccessContainer },
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
        Column(modifier = Modifier.fillMaxWidth()) {
            QRAppSnackbar(
                QRAppSnackbarVisuals(
                    type = SnackbarType.INFO,
                    message = "This is an informative snackbar, not to be used for critical messages",
                ),
            )
        }
    }
}

@Preview
@Composable
fun QRAppSnackbarSuccessPreview() {
    BaseQRCodeAppPreview {
        Column(modifier = Modifier.fillMaxWidth()) {
            QRAppSnackbar(
                QRAppSnackbarVisuals(
                    type = SnackbarType.SUCCESS,
                    message = "This is a success snackbar, when an action the user takes is successful",
                ),
            )
        }
    }
}

@Preview
@Composable
fun QRAppSnackbarErrorPreview() {
    BaseQRCodeAppPreview {
        Column(modifier = Modifier.fillMaxWidth()) {
            QRAppSnackbar(
                QRAppSnackbarVisuals(
                    type = SnackbarType.ERROR,
                    message =
                        "This is an error snackbar, when an action the user takes is NOT successful," +
                            " or just something going wrong in general",
                ),
            )
        }
    }
}
// endregion previews
