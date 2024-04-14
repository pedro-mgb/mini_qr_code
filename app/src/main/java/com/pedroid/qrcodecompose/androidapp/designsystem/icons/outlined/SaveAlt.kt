package com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.tooling.preview.Preview

/*
Credits: Generated using https://github.com/DenisMondon/Svg2Compose
Original Svg: https://materialui.co/material-icons-outlined/save-alt
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */

val Icons.Outlined.SaveAlt: ImageVector
    get() {
        if (saveAlt != null) {
            return saveAlt!!
        }
        saveAlt =
            materialIcon(name = "SaveAlt") {
                path(
                    fill = SolidColor(Color.White),
                    fillAlpha = 1.0F,
                    strokeAlpha = 1.0F,
                    strokeLineWidth = 0.0F,
                    strokeLineCap = StrokeCap.Butt,
                    strokeLineJoin = StrokeJoin.Miter,
                    strokeLineMiter = 4.0F,
                    pathFillType = PathFillType.NonZero,
                ) {
                    moveTo(19.0F, 12.0F)
                    verticalLineToRelative(7.0F)
                    lineTo(5.0F, 19.0F)
                    verticalLineToRelative(-7.0F)
                    lineTo(3.0F, 12.0F)
                    verticalLineToRelative(7.0F)
                    curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                    horizontalLineToRelative(14.0F)
                    curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
                    verticalLineToRelative(-7.0F)
                    horizontalLineToRelative(-2.0F)

                    moveTo(13.0F, 12.67F)
                    lineToRelative(2.59F, -2.58F)
                    lineTo(17.0F, 11.5F)
                    lineToRelative(-5.0F, 5.0F)
                    lineToRelative(-5.0F, -5.0F)
                    lineToRelative(1.41F, -1.41F)
                    lineTo(11.0F, 12.67F)
                    lineTo(11.0F, 3.0F)
                    horizontalLineToRelative(2.0F)
                    close()
                }
            }
        return saveAlt!!
    }

private var saveAlt: ImageVector? = null

@Preview
@Composable
private fun IconSaveAltPreview() {
    Image(imageVector = Icons.Outlined.SaveAlt, contentDescription = null)
}
