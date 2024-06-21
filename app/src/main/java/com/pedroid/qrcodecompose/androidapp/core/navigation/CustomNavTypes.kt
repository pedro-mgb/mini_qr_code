package com.pedroid.qrcodecompose.androidapp.core.navigation

import android.os.Build
import android.os.Bundle
import androidx.navigation.NavType
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

/**
 * Necessary because by default Jetpack Navigation only supports specific navtypes
 *
 * Credits: https://github.com/ioannisa/TypeSafeComposeNavigation/blob/main/app/src/main/java/eu/anifantakis/navdemo/navigation/NavRoute.kt#L22
 */
fun NavType.Companion.ofQRCodeComposeXFormat(): NavType<QRCodeComposeXFormat> {
    return object : NavType<QRCodeComposeXFormat>(isNullableAllowed = false) {
        override fun get(
            bundle: Bundle,
            key: String,
        ): QRCodeComposeXFormat? =
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                @Suppress("DEPRECATION")
                bundle.getSerializable(key) as? QRCodeComposeXFormat
            } else {
                bundle.getSerializable(key, QRCodeComposeXFormat::class.java)
            }

        override fun parseValue(value: String): QRCodeComposeXFormat = QRCodeComposeXFormat.valueOf(value)

        override fun put(
            bundle: Bundle,
            key: String,
            value: QRCodeComposeXFormat,
        ) = bundle.putSerializable(key, value)
    }
}
