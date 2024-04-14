package com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

/*
Credits: Generated using https://github.com/DenisMondon/Svg2Compose
Original Svg: https://materialui.co/icon/image
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */
val Icons.Filled.ImageFile: ImageVector
    get() {
        if (filledImageFile != null) {
            return filledImageFile!!
        }
        filledImageFile =
            materialIcon(name = "ImageFile") {
                materialPath {
                    moveTo(21.0F, 19.0F)
                    verticalLineTo(5.0F)
                    curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)
                    horizontalLineTo(5.0F)
                    curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                    verticalLineToRelative(14.0F)
                    curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                    horizontalLineToRelative(14.0F)
                    curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)

                    moveTo(8.5F, 13.5F)
                    lineToRelative(2.5F, 3.01F)
                    lineTo(14.5F, 12.0F)
                    lineToRelative(4.5F, 6.0F)
                    horizontalLineTo(5.0F)
                    lineToRelative(3.5F, -4.5F)
                    close()
                }
            }
        return filledImageFile!!
    }

private var filledImageFile: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconImageFilePreview() {
    Image(imageVector = Icons.Filled.ImageFile, contentDescription = null)
}
