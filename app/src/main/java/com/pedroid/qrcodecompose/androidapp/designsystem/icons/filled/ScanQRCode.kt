package com.pedroid.qrcodecompose.androidapp.designsystem.icons.filled

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
Original Svg: https://materialui.co/icon/qr-code-scanner
 */

val Icons.Filled.ScanQRCode: ImageVector
    get() {
        if (_filledScanQRCode != null) {
            return _filledScanQRCode!!
        }
        _filledScanQRCode = materialIcon(name = "Filled.ScanQRCode") {
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
                moveTo(9.5F, 6.5F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(-3.0F)
                verticalLineToRelative(-3.0F)
                horizontalLineTo(9.5F)
                moveTo(11.0F, 5.0F)
                horizontalLineTo(5.0F)
                verticalLineToRelative(6.0F)
                horizontalLineToRelative(6.0F)
                verticalLineTo(5.0F)
                lineTo(11.0F, 5.0F)

                moveTo(9.5F, 14.5F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(-3.0F)
                verticalLineToRelative(-3.0F)
                horizontalLineTo(9.5F)
                moveTo(11.0F, 13.0F)
                horizontalLineTo(5.0F)
                verticalLineToRelative(6.0F)
                horizontalLineToRelative(6.0F)
                verticalLineTo(13.0F)
                lineTo(11.0F, 13.0F)

                moveTo(17.5F, 6.5F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(-3.0F)
                verticalLineToRelative(-3.0F)
                horizontalLineTo(17.5F)
                moveTo(19.0F, 5.0F)
                horizontalLineToRelative(-6.0F)
                verticalLineToRelative(6.0F)
                horizontalLineToRelative(6.0F)
                verticalLineTo(5.0F)
                lineTo(19.0F, 5.0F)

                moveTo(13.0F, 13.0F)
                horizontalLineToRelative(1.5F)
                verticalLineToRelative(1.5F)
                horizontalLineTo(13.0F)
                verticalLineTo(13.0F)

                moveTo(14.5F, 14.5F)
                horizontalLineTo(16.0F)
                verticalLineTo(16.0F)
                horizontalLineToRelative(-1.5F)
                verticalLineTo(14.5F)

                moveTo(16.0F, 13.0F)
                horizontalLineToRelative(1.5F)
                verticalLineToRelative(1.5F)
                horizontalLineTo(16.0F)
                verticalLineTo(13.0F)

                moveTo(13.0F, 16.0F)
                horizontalLineToRelative(1.5F)
                verticalLineToRelative(1.5F)
                horizontalLineTo(13.0F)
                verticalLineTo(16.0F)

                moveTo(14.5F, 17.5F)
                horizontalLineTo(16.0F)
                verticalLineTo(19.0F)
                horizontalLineToRelative(-1.5F)
                verticalLineTo(17.5F)

                moveTo(16.0F, 16.0F)
                horizontalLineToRelative(1.5F)
                verticalLineToRelative(1.5F)
                horizontalLineTo(16.0F)
                verticalLineTo(16.0F)

                moveTo(17.5F, 14.5F)
                horizontalLineTo(19.0F)
                verticalLineTo(16.0F)
                horizontalLineToRelative(-1.5F)
                verticalLineTo(14.5F)

                moveTo(17.5F, 17.5F)
                horizontalLineTo(19.0F)
                verticalLineTo(19.0F)
                horizontalLineToRelative(-1.5F)
                verticalLineTo(17.5F)

                moveTo(22.0F, 7.0F)
                horizontalLineToRelative(-2.0F)
                verticalLineTo(4.0F)
                horizontalLineToRelative(-3.0F)
                verticalLineTo(2.0F)
                horizontalLineToRelative(5.0F)
                verticalLineTo(7.0F)

                moveTo(22.0F, 22.0F)
                verticalLineToRelative(-5.0F)
                horizontalLineToRelative(-2.0F)
                verticalLineToRelative(3.0F)
                horizontalLineToRelative(-3.0F)
                verticalLineToRelative(2.0F)
                horizontalLineTo(22.0F)

                moveTo(2.0F, 22.0F)
                horizontalLineToRelative(5.0F)
                verticalLineToRelative(-2.0F)
                horizontalLineTo(4.0F)
                verticalLineToRelative(-3.0F)
                horizontalLineTo(2.0F)
                verticalLineTo(22.0F)

                moveTo(2.0F, 2.0F)
                verticalLineToRelative(5.0F)
                horizontalLineToRelative(2.0F)
                verticalLineTo(4.0F)
                horizontalLineToRelative(3.0F)
                verticalLineTo(2.0F)
                horizontalLineTo(2.0F)
                close()
            }
        }
        return _filledScanQRCode!!
    }

private var _filledScanQRCode: ImageVector? = null

@Preview
@Composable
private fun IconFilledScanQRCodePreview() {
    Image(imageVector = Icons.Filled.ScanQRCode, contentDescription = null)
}