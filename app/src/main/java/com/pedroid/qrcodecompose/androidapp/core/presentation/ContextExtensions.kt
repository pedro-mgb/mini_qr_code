package com.pedroid.qrcodecompose.androidapp.core.presentation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions

private const val VIBRATION_DURATION_MILLIS = 300L

fun Context.openAppToView(content: String): QRAppActions.OpenApp {
    return try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(content)))
        QRAppActions.OpenApp(ActionStatus.SUCCESS)
    } catch (e: ActivityNotFoundException) {
        QRAppActions.OpenApp(ActionStatus.ERROR_NO_APP)
    }
}

fun Context.launchBrowserCustomTab(
    content: String,
    toolbarColor: Int,
    openWithViewIntentFallback: Boolean = true,
): QRAppActions.OpenApp {
    val isUrl = Patterns.WEB_URL.matcher(content).matches()
    return if (!isUrl || CustomTabsClient.getPackageName(this, emptyList()) == null) {
        if (openWithViewIntentFallback) {
            openAppToView(content)
        } else {
            QRAppActions.OpenApp(ActionStatus.ERROR_NO_APP)
        }
    } else {
        val customTabBarColor =
            CustomTabColorSchemeParams.Builder()
                .setToolbarColor(toolbarColor).build()
        CustomTabsIntent.Builder()
        val customTabsIntent =
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(customTabBarColor)
                .build()
        customTabsIntent.launchUrl(this, Uri.parse(content))
        QRAppActions.OpenApp(ActionStatus.SUCCESS)
    }
}

fun Context.shareTextToAnotherApp(
    content: String,
    shareTitle: String? = null,
): QRAppActions.ShareApp {
    return try {
        val sendIntent: Intent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, content)
                type = "text/plain"
            }
        val shareIntent = Intent.createChooser(sendIntent, shareTitle)
        startActivity(shareIntent)
        QRAppActions.ShareApp(ActionStatus.SUCCESS)
    } catch (e: ActivityNotFoundException) {
        QRAppActions.ShareApp(ActionStatus.ERROR_NO_APP)
    }
}

fun Context.shareImageToAnotherApp(
    content: Bitmap?,
    shareTitle: String,
): QRAppActions.ShareApp {
    val cachedImageUri =
        saveImageInCache(content, shareTitle) ?: return QRAppActions.ShareApp(
            ActionStatus.ERROR_FILE,
        )
    return try {
        val sendIntent: Intent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, cachedImageUri)
                type = IMAGE_MIME_TYPE
            }
        val shareIntent = Intent.createChooser(sendIntent, shareTitle)
        startActivity(shareIntent)
        QRAppActions.ShareApp(ActionStatus.SUCCESS)
    } catch (e: ActivityNotFoundException) {
        QRAppActions.ShareApp(ActionStatus.ERROR_NO_APP)
    }
}

fun Context.copyTextToClipboard(
    text: String,
    auxiliaryLabel: String = "",
) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
        val clip = ClipData.newPlainText(auxiliaryLabel, text)
        setPrimaryClip(clip)
    }
}

fun Context.copyImageToClipboard(
    bitmap: Bitmap?,
    auxiliaryLabel: String = "",
): QRAppActions.Copy {
    val cachedImageUri = saveImageInCache(bitmap, "Image Clipboard") ?: return QRAppActions.Copy(ActionStatus.ERROR_FILE)
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
        val clip = ClipData.newUri(contentResolver, auxiliaryLabel, cachedImageUri)
        setPrimaryClip(clip)
    }
    return QRAppActions.Copy(ActionStatus.SUCCESS)
}

fun Context.sendEmail(emailAddress: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.setData(Uri.parse("mailto:$emailAddress"))
    this.startActivity(emailIntent)
}

fun Context.showToast(
    @StringRes stringId: Int,
) {
    Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(string: String) {
    Toast.makeText(this, getString(string), Toast.LENGTH_SHORT).show()
}

fun Context.shortPhoneVibration() {
    val vibrator: Vibrator =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
            /*
            Note: For Android 10 (AKA Android Q) and above there's a VibrationEffect.createPredefined(VibrationEffect.DEFAULT_AMPLITUDE)
            However it doesn't seem to work for all manufacturers
            E.g. in Huawei Android 10, it crashes, hence we are using the slightly older API, but with the same effect, and here we can control the duration
             */
            vibrator.cancel()
            vibrator.vibrate(VibrationEffect.createOneShot(VIBRATION_DURATION_MILLIS, VibrationEffect.DEFAULT_AMPLITUDE))
        }
        else -> {
            vibrator.cancel()
            @Suppress("DEPRECATION")
            vibrator.vibrate(VIBRATION_DURATION_MILLIS)
        }
    }
}

/**
 * @return string in resources associated with supplied key, or the actual key if the resource is not found
 *
 * useful if the caller is uncertain that the current string is a key or an actual text
 */
@SuppressLint("DiscouragedApi")
fun Context.getString(key: String): String {
    val resId: Int = resources.getIdentifier(key, "string", packageName)
    return if (resId > 0) {
        try {
            getString(resId)
        } catch (e: Resources.NotFoundException) {
            key
        }
    } else {
        key
    }
}
