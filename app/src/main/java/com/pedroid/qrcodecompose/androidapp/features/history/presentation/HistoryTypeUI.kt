package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.Camera
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled.ImageFile
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.onSuccessContainer
import com.pedroid.qrcodecompose.androidapp.features.history.domain.HistoryEntry

enum class HistoryTypeUI(
    @StringRes val typeStringId: Int,
    @StringRes val typeDetailsStringId: Int,
    val icon: ImageVector,
    val color: @Composable () -> Color,
) {
    SCAN_CAMERA(
        R.string.history_item_scan_camera,
        R.string.history_detail_scan_camera_at,
        Icons.Filled.Camera,
        { MaterialTheme.colorScheme.onPrimaryContainer },
    ),
    SCAN_FILE(
        R.string.history_item_scan_image_file,
        R.string.history_detail_scan_file_at,
        Icons.Filled.ImageFile,
        { MaterialTheme.colorScheme.onPrimaryContainer },
    ),
    GENERATE(
        R.string.history_item_generate,
        R.string.history_detail_generated_at,
        Icons.Filled.AddCircle,
        { MaterialTheme.colorScheme.onSuccessContainer },
    ),
}

fun HistoryEntry.getTypeUI(): HistoryTypeUI =
    when (this) {
        is HistoryEntry.Scan -> {
            if (!this.fromImageFile) {
                HistoryTypeUI.SCAN_CAMERA
            } else {
                HistoryTypeUI.SCAN_FILE
            }
        }
        is HistoryEntry.Generate -> HistoryTypeUI.GENERATE
    }
