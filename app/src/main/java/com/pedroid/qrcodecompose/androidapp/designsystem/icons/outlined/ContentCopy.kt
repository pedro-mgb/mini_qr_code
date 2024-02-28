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
Original Svg: https://materialui.co/material-icons-outlined/content-copy
 */

val Icons.Outlined.ContentCopy: ImageVector
    get() {
        if (contentCopy != null) {
            return contentCopy!!
        }
        contentCopy =
            materialIcon(name = "Outlined.ContentCopy") {
                materialPath {
                    moveTo(16.0F, 1.0F)
                    lineTo(4.0F, 1.0F)
                    curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                    verticalLineToRelative(14.0F)
                    horizontalLineToRelative(2.0F)
                    lineTo(4.0F, 3.0F)
                    horizontalLineToRelative(12.0F)
                    lineTo(16.0F, 1.0F)

                    moveTo(19.0F, 5.0F)
                    lineTo(8.0F, 5.0F)
                    curveToRelative(-1.1F, 0.0F, -2.0F, 0.9F, -2.0F, 2.0F)
                    verticalLineToRelative(14.0F)
                    curveToRelative(0.0F, 1.1F, 0.9F, 2.0F, 2.0F, 2.0F)
                    horizontalLineToRelative(11.0F)
                    curveToRelative(1.1F, 0.0F, 2.0F, -0.9F, 2.0F, -2.0F)
                    lineTo(21.0F, 7.0F)
                    curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)

                    moveTo(19.0F, 21.0F)
                    lineTo(8.0F, 21.0F)
                    lineTo(8.0F, 7.0F)
                    horizontalLineToRelative(11.0F)
                    verticalLineToRelative(14.0F)
                    close()
                }
            }
        return contentCopy!!
    }

private var contentCopy: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconContentCopyPreview() {
    Image(imageVector = Icons.Outlined.ContentCopy, contentDescription = null)
}
