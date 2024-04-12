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
Original Svg: https://materialui.co/icon/image
 */

val Icons.Filled.ImageFile: ImageVector
    get() {
        if (filledImageFile != null) {
            return filledImageFile!!
        }
        filledImageFile =
            ImageVector.Builder(
                name = "ImageFile",
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

                moveTo(19.0F, 12.0F)
                verticalLineToRelative(7.0F)
                horizontalLineTo(5.0F)
                verticalLineToRelative(-7.0F)
                horizontalLineTo(3.0F)
                verticalLineToRelative(7.0F)
                horizontalLineToRelative(14.0F)
                verticalLineToRelative(-7.0F)
                horizontalLineToRelative(-2.0F)

                moveToRelative(-6.0F, 0.67F)
                lineTo(17.0F, 11.5F)
                lineTo(11.0F, 12.67F)
                verticalLineTo(3.0F)
                horizontalLineToRelative(2.0F)

                close()
            }.build()
        return filledImageFile!!
    }
private var filledImageFile: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconImageFilePreview() {
    Image(imageVector = Icons.Filled.ImageFile, contentDescription = null)
}
