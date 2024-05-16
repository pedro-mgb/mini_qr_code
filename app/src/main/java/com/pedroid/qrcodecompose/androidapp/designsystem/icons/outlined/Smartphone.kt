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
Original Svg: https://materialui.co/material-icons-outlined/smartphone
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */

val Icons.Outlined.Smartphone: ImageVector
    get() {
        if (smartphoneAlt != null) {
            return smartphoneAlt!!
        }
        smartphoneAlt =
            materialIcon(name = "Smartphone") {
                materialPath {
                    moveTo(17.0F, 1.01F)
                    lineTo(7.0F, 1.0F)
                    curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                    verticalLineToRelative(18.0F)
                    curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                    horizontalLineToRelative(10.0F)
                    curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
                    verticalLineTo(3.0F)
                    curveToRelative(0.0F, -1.1F, -0.9F, -1.99F, -2.0F, -1.99F)

                    moveTo(17.0F, 19.0F)
                    horizontalLineTo(7.0F)
                    verticalLineTo(5.0F)
                    horizontalLineToRelative(10.0F)
                    verticalLineToRelative(14.0F)
                    close()
                }
            }
        return smartphoneAlt!!
    }

private var smartphoneAlt: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconSmartphonePreview() {
    Image(imageVector = Icons.Outlined.Smartphone, contentDescription = null)
}
