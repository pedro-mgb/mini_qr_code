package com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

/*
Credits: Generated using https://github.com/DenisMondon/Svg2Compose
Original Svg: https://materialui.co/material-icons-outlined/vibration
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */

val Icons.Outlined.Vibration: ImageVector
    get() {
        if (vibrationAlt != null) {
            return vibrationAlt!!
        }
        vibrationAlt =
            materialIcon(name = "Vibration") {
                materialPath {
                    moveTo(0.0F, 15.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(2.0F, 9.0F)
                    lineTo(0.0F, 9.0F)
                    verticalLineToRelative(6.0F)

                    moveTo(3.0F, 17.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(5.0F, 7.0F)
                    lineTo(3.0F, 7.0F)
                    verticalLineToRelative(10.0F)

                    moveTo(22.0F, 9.0F)
                    verticalLineToRelative(6.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(24.0F, 9.0F)
                    horizontalLineToRelative(-2.0F)

                    moveTo(19.0F, 17.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(21.0F, 7.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(10.0F)

                    moveTo(16.5F, 3.0F)
                    horizontalLineToRelative(-9.0F)
                    curveTo(6.67F, 3.0F, 6.0F, 3.67F, 6.0F, 4.5F)
                    verticalLineToRelative(15.0F)
                    curveToRelative(0.0F, 0.83F, 0.67F, 1.5F, 1.5F, 1.5F)
                    horizontalLineToRelative(9.0F)
                    curveToRelative(0.83F, 0.0F, 1.5F, -0.67F, 1.5F, -1.5F)
                    verticalLineToRelative(-15.0F)
                    curveToRelative(0.0F, -0.83F, -0.67F, -1.5F, -1.5F, -1.5F)

                    moveTo(16.0F, 19.0F)
                    lineTo(8.0F, 19.0F)
                    lineTo(8.0F, 5.0F)
                    horizontalLineToRelative(8.0F)
                    verticalLineToRelative(14.0F)
                    close()
                }
            }
        return vibrationAlt!!
    }

private var vibrationAlt: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconVibrationPreview() {
    Image(imageVector = Icons.Outlined.Vibration, contentDescription = null)
}
