package com.pedroid.qrcodecompose.androidapp.features.expand.navigation

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
@Parcelize
data class ExpandQRCodeArguments(
    val label: String,
    val code: String,
    val format: QRCodeComposeXFormat,
) : Parcelable

/**
 * Necessary because by default Jetpack Navigation only supports specific navtypes
 *
 * Credits: https://github.com/ioannisa/TypeSafeComposeNavigation/blob/main/app/src/main/java/eu/anifantakis/navdemo/navigation/NavRoute.kt#L22
 */
fun NavType.Companion.ofExpandQRCodeArguments(): NavType<ExpandQRCodeArguments> {
    return object : NavType<ExpandQRCodeArguments>(isNullableAllowed = false) {
        override fun get(
            bundle: Bundle,
            key: String,
        ): ExpandQRCodeArguments? =
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.TIRAMISU) {
                @Suppress("DEPRECATION")
                bundle.getParcelable(key) as? ExpandQRCodeArguments
            } else {
                bundle.getParcelable(key, ExpandQRCodeArguments::class.java)
            }

        override fun parseValue(value: String): ExpandQRCodeArguments = Json.decodeFromString(value)

        override fun put(
            bundle: Bundle,
            key: String,
            value: ExpandQRCodeArguments,
        ) = bundle.putParcelable(key, value)
    }
}
