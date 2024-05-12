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
Original Svg: https://materialui.co/material-icons-outlined/language
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */
val Icons.Outlined.Browser: ImageVector
    get() {
        if (browserAlt != null) {
            return browserAlt!!
        }
        browserAlt =
            materialIcon(name = "Browser") {
                materialPath {
                    moveTo(11.99F, 2.0F)
                    curveTo(6.47F, 2.0F, 2.0F, 6.48F, 2.0F, 12.0F)
                    reflectiveCurveToRelative(4.47F, 10.0F, 9.99F, 10.0F)
                    curveTo(17.52F, 22.0F, 22.0F, 17.52F, 22.0F, 12.0F)
                    reflectiveCurveTo(17.52F, 2.0F, 11.99F, 2.0F)

                    moveTo(18.92F, 8.0F)
                    horizontalLineToRelative(-2.95F)
                    curveToRelative(-0.32F, -1.25F, -0.78F, -2.45F, -1.38F, -3.56F)
                    curveToRelative(1.84F, 0.63F, 3.37F, 1.91F, 4.33F, 3.56F)

                    moveTo(12.0F, 4.04F)
                    curveToRelative(0.83F, 1.2F, 1.48F, 2.53F, 1.91F, 3.96F)
                    horizontalLineToRelative(-3.82F)
                    curveToRelative(0.43F, -1.43F, 1.08F, -2.76F, 1.91F, -3.96F)

                    moveTo(4.26F, 14.0F)
                    curveTo(4.1F, 13.36F, 4.0F, 12.69F, 4.0F, 12.0F)
                    reflectiveCurveToRelative(0.1F, -1.36F, 0.26F, -2.0F)
                    horizontalLineToRelative(3.38F)
                    curveToRelative(-0.08F, 0.66F, -0.14F, 1.32F, -0.14F, 2.0F)
                    curveToRelative(0.0F, 0.68F, 0.06F, 1.34F, 0.14F, 2.0F)
                    lineTo(4.26F, 14.0F)

                    moveTo(5.08F, 16.0F)
                    horizontalLineToRelative(2.95F)
                    curveToRelative(0.32F, 1.25F, 0.78F, 2.45F, 1.38F, 3.56F)
                    curveToRelative(-1.84F, -0.63F, -3.37F, -1.9F, -4.33F, -3.56F)

                    moveTo(8.03F, 8.0F)
                    lineTo(5.08F, 8.0F)
                    curveToRelative(0.96F, -1.66F, 2.49F, -2.93F, 4.33F, -3.56F)
                    curveTo(8.81F, 5.55F, 8.35F, 6.75F, 8.03F, 8.0F)

                    moveTo(12.0F, 19.96F)
                    curveToRelative(-0.83F, -1.2F, -1.48F, -2.53F, -1.91F, -3.96F)
                    horizontalLineToRelative(3.82F)
                    curveToRelative(-0.43F, 1.43F, -1.08F, 2.76F, -1.91F, 3.96F)

                    moveTo(14.34F, 14.0F)
                    lineTo(9.66F, 14.0F)
                    curveToRelative(-0.09F, -0.66F, -0.16F, -1.32F, -0.16F, -2.0F)
                    curveToRelative(0.0F, -0.68F, 0.07F, -1.35F, 0.16F, -2.0F)
                    horizontalLineToRelative(4.68F)
                    curveToRelative(0.09F, 0.65F, 0.16F, 1.32F, 0.16F, 2.0F)
                    curveToRelative(0.0F, 0.68F, -0.07F, 1.34F, -0.16F, 2.0F)

                    moveTo(14.59F, 19.56F)
                    curveToRelative(0.6F, -1.11F, 1.06F, -2.31F, 1.38F, -3.56F)
                    horizontalLineToRelative(2.95F)
                    curveToRelative(-0.96F, 1.65F, -2.49F, 2.93F, -4.33F, 3.56F)

                    moveTo(16.36F, 14.0F)
                    curveToRelative(0.08F, -0.66F, 0.14F, -1.32F, 0.14F, -2.0F)
                    curveToRelative(0.0F, -0.68F, -0.06F, -1.34F, -0.14F, -2.0F)
                    horizontalLineToRelative(3.38F)
                    curveToRelative(0.16F, 0.64F, 0.26F, 1.31F, 0.26F, 2.0F)
                    reflectiveCurveToRelative(-0.1F, 1.36F, -0.26F, 2.0F)
                    horizontalLineToRelative(-3.38F)
                    close()
                }
            }
        return browserAlt!!
    }

private var browserAlt: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconLanguagePreview() {
    Image(imageVector = Icons.Outlined.Browser, contentDescription = null)
}
