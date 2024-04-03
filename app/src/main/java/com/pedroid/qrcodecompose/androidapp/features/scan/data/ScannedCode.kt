package com.pedroid.qrcodecompose.androidapp.features.scan.data

import android.os.Parcelable
import androidx.annotation.StringRes
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScannedCode(
    val data: String,
    val format: QRCodeComposeXFormat,
    val source: ScanSource,
) : Parcelable

enum class ScanSource(
    @StringRes val scannedLabelId: Int,
) {
    CAMERA(R.string.scan_code_read_camera_label),
    IMAGE_FILE(R.string.scan_code_read_from_file_label),
}
