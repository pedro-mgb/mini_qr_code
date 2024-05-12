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
Original Svg: https://materialui.co/material-icons-outlined/contact-support
First converted to xml vector drawable, then to compose image vector
This is because direct conversion of svg to image vector in the above software was not working properly
 */

val Icons.Outlined.ContactSupport: ImageVector
    get() {
        if (contactSupportAlt != null) {
            return contactSupportAlt!!
        }
        contactSupportAlt =
            materialIcon(name = "ContactSupport") {
                materialPath {
                    moveTo(11.5F, 2.0F)
                    curveTo(6.81F, 2.0F, 3.0F, 5.81F, 3.0F, 10.5F)
                    reflectiveCurveTo(6.81F, 19.0F, 11.5F, 19.0F)
                    horizontalLineToRelative(0.5F)
                    verticalLineToRelative(3.0F)
                    curveToRelative(4.86F, -2.34F, 8.0F, -7.0F, 8.0F, -11.5F)
                    curveTo(20.0F, 5.81F, 16.19F, 2.0F, 11.5F, 2.0F)

                    moveTo(12.5F, 16.5F)
                    horizontalLineToRelative(-2.0F)
                    verticalLineToRelative(-2.0F)
                    horizontalLineToRelative(2.0F)
                    verticalLineToRelative(2.0F)

                    moveTo(12.5F, 13.0F)
                    horizontalLineToRelative(-2.0F)
                    curveToRelative(0.0F, -3.25F, 3.0F, -3.0F, 3.0F, -5.0F)
                    curveToRelative(0.0F, -1.1F, -0.9F, -2.0F, -2.0F, -2.0F)
                    reflectiveCurveToRelative(-2.0F, 0.9F, -2.0F, 2.0F)
                    horizontalLineToRelative(-2.0F)
                    curveToRelative(0.0F, -2.21F, 1.79F, -4.0F, 4.0F, -4.0F)
                    reflectiveCurveToRelative(4.0F, 1.79F, 4.0F, 4.0F)
                    curveToRelative(0.0F, 2.5F, -3.0F, 2.75F, -3.0F, 5.0F)
                    close()
                }
            }
        return contactSupportAlt!!
    }

private var contactSupportAlt: ImageVector? = null

@Preview
@Composable
@Suppress("UnusedPrivateMember")
private fun IconContactSupportPreview() {
    Image(imageVector = Icons.Outlined.ContactSupport, contentDescription = null)
}
