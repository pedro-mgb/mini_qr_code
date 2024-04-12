package com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
Credits: Generated using https://github.com/DenisMondon/Svg2Compose
Original Svg: https://materialui.co/icon/photo-camera
 */

val Icons.Filled.Camera: ImageVector
    get() {
        if (filledCamera != null) {
            return filledCamera!!
        }
        filledCamera =
            ImageVector.Builder(
                name = "Camera",
                defaultWidth = 200.0.dp,
                defaultHeight = 200.0.dp,
                viewportWidth = 200.0F,
                viewportHeight = 200.0F,
            ).materialPath {
                verticalLineToRelative(0.0F)
                moveTo(0.0F, 0.0F)
                horizontalLineToRelative(24.0F)
                verticalLineToRelative(24.0F)
                horizontalLineTo(0.0F)

                moveTo(9.0F, 2.0F)
                lineTo(7.17F, 4.0F)
                horizontalLineTo(4.0F)
                verticalLineToRelative(12.0F)
                horizontalLineToRelative(16.0F)
                verticalLineTo(6.0F)
                horizontalLineToRelative(-3.17F)
                lineTo(15.0F, 2.0F)
                horizontalLineTo(9.0F)

                moveToRelative(3.0F, 15.0F)
                reflectiveCurveToRelative(5.0F, 2.24F, 5.0F, 5.0F)

                close()
            }.build()
        return filledCamera!!
    }
private var filledCamera: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconCameraPreview() {
    Image(imageVector = Icons.Filled.Camera, contentDescription = null)
}
