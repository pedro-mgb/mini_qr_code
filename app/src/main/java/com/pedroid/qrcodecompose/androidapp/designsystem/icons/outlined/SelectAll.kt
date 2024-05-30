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
Original Svg: https://materialui.co/material-icons-outlined/select-all
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */

val Icons.Outlined.SelectAll: ImageVector
    get() {
        if (selectAll != null) {
            return selectAll!!
        }
        selectAll =
            materialIcon(name = "SelectAll") {
                materialPath {
                    moveTo(3.0F, 5.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(5.0F, 3.0F)
                    curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)

                    moveTo(3.0F, 13.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    lineTo(3.0F, 11.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(7.0F, 21.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    lineTo(7.0F, 19.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(3.0F, 9.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(5.0F, 7.0F)
                    lineTo(3.0F, 7.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(13.0F, 3.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(13.0F, 3.0F)

                    moveTo(19.0F, 3.0F)
                    verticalLineToRelative(2.0F)
                    horizontalLineToRelative(2.0F)
                    curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)

                    moveTo(5.0F, 21.0F)
                    verticalLineToRelative(-2.0F)
                    lineTo(3.0F, 19.0F)
                    curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)

                    moveTo(3.0F, 17.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    lineTo(3.0F, 15.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(9.0F, 3.0F)
                    lineTo(7.0F, 3.0F)
                    verticalLineToRelative(2.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(9.0F, 3.0F)

                    moveTo(11.0F, 21.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(19.0F, 13.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(19.0F, 21.0F)
                    curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(19.0F, 9.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(21.0F, 7.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(19.0F, 17.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(15.0F, 21.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(-2.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(15.0F, 5.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(17.0F, 3.0F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(7.0F, 17.0F)
                    horizontalLineToRelative(10.0F)
                    lineTo(17.0F, 7.0F)
                    lineTo(7.0F, 7.0F)
                    verticalLineToRelative(10.0F)

                    moveTo(9.0F, 9.0F)
                    horizontalLineToRelative(6.0F)
                    verticalLineToRelative(6.0F)
                    lineTo(9.0F, 15.0F)
                    lineTo(9.0F, 9.0F)
                    close()
                }
            }
        return selectAll!!
    }

private var selectAll: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconSelectAllPreview() {
    Image(imageVector = Icons.Outlined.SelectAll, contentDescription = null)
}
