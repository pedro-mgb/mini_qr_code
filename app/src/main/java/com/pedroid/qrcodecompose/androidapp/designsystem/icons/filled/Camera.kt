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
Original Svg: https://materialui.co/icon/photo-camera
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */

val Icons.Filled.Camera: ImageVector
    get() {
        if (filledCamera != null) {
            return filledCamera!!
        }
        filledCamera =
            materialIcon(name = "Camera") {
                materialPath {
                    moveTo(12.0F, 12.0F)
                    moveToRelative(-3.2F, 0.0F)
                    arcToRelative(3.2F, 3.2F, 0.0F, true, true, 6.4F, 0.0F)
                    arcToRelative(3.2F, 3.2F, 0.0F, true, true, -6.4F, 0.0F)
                    close()
                }
                materialPath {
                    moveTo(9.0F, 2.0F)
                    lineTo(7.17F, 4.0F)
                    lineTo(4.0F, 4.0F)
                    curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                    verticalLineToRelative(12.0F)
                    curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                    horizontalLineToRelative(16.0F)
                    curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
                    lineTo(22.0F, 6.0F)
                    curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)
                    horizontalLineToRelative(-3.17F)
                    lineTo(15.0F, 2.0F)
                    lineTo(9.0F, 2.0F)

                    moveTo(12.0F, 17.0F)
                    curveToRelative(-2.76F, 0.0F, -5.0F, -2.24F, -5.0F, -5.0F)
                    reflectiveCurveToRelative(2.24F, -5.0F, 5.0F, -5.0F)
                    reflectiveCurveToRelative(5.0F, 2.24F, 5.0F, 5.0F)
                    reflectiveCurveToRelative(-2.24F, 5.0F, -5.0F, 5.0F)
                    close()
                }
            }
        return filledCamera!!
    }

private var filledCamera: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconCameraPreview() {
    Image(imageVector = Icons.Filled.Camera, contentDescription = null)
}
