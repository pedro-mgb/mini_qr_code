package com.pedroid.qrcodecompose.androidapp.features.history.presentation

import android.os.Parcelable
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
import kotlinx.parcelize.Parcelize

sealed class HistoryListItem : Parcelable {
    @Parcelize
    data class SectionHeader(val text: String) : HistoryListItem()

    @Parcelize
    data class Data(
        val uid: Long,
        val value: String,
        val displayDate: String,
        val typeUI: HistoryTypeUI,
        @StringRes val formatStringId: Int,
    ) : HistoryListItem()
}

enum class HistoryTypeUI(
    @StringRes val typeStringId: Int,
    val icon: ImageVector,
    val color: @Composable () -> Color,
) {
    SCAN_CAMERA(
        R.string.history_item_scan_camera,
        Icons.Filled.Camera,
        { MaterialTheme.colorScheme.onPrimaryContainer },
    ),
    SCAN_FILE(
        R.string.history_item_scan_image_file,
        Icons.Filled.ImageFile,
        { MaterialTheme.colorScheme.onPrimaryContainer },
    ),
    GENERATE(
        R.string.history_item_generate,
        Icons.Filled.AddCircle,
        { MaterialTheme.colorScheme.onSuccessContainer },
    ),
}
