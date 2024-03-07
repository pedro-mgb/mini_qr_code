package com.pedroid.qrcodecompose.androidapp.core.presentation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.openAppToView(content: String): ExternalAppStartResponse.OpenApp {
    return try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(content)))
        ExternalAppStartResponse.OpenApp(AppResponseStatus.SUCCESS)
    } catch (e: ActivityNotFoundException) {
        ExternalAppStartResponse.OpenApp(AppResponseStatus.ERROR_NO_APP)
    }
}

fun Context.shareTextToAnotherApp(
    content: String,
    shareTitle: String? = null,
): ExternalAppStartResponse.ShareApp {
    return try {
        val sendIntent: Intent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_TEXT, content)
                type = "text/plain"
            }
        val shareIntent = Intent.createChooser(sendIntent, shareTitle)
        startActivity(shareIntent)
        ExternalAppStartResponse.ShareApp(AppResponseStatus.SUCCESS)
    } catch (e: ActivityNotFoundException) {
        ExternalAppStartResponse.ShareApp(AppResponseStatus.ERROR_NO_APP)
    }
}

fun Context.shareImageToAnotherApp(
    content: Bitmap?,
    shareTitle: String,
): ExternalAppStartResponse.ShareApp {
    val cachedImageUri = saveImageInCache(content, shareTitle) ?: return ExternalAppStartResponse.ShareApp(AppResponseStatus.ERROR_FILE)
    return try {
        val sendIntent: Intent =
            Intent(Intent.ACTION_SEND).apply {
                putExtra(Intent.EXTRA_STREAM, cachedImageUri)
                type = IMAGE_MIME_TYPE
            }
        val shareIntent = Intent.createChooser(sendIntent, shareTitle)
        startActivity(shareIntent)
        ExternalAppStartResponse.ShareApp(AppResponseStatus.SUCCESS)
    } catch (e: ActivityNotFoundException) {
        ExternalAppStartResponse.ShareApp(AppResponseStatus.ERROR_NO_APP)
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
): Boolean {
    val cachedImageUri = saveImageInCache(bitmap, "Image Clipboard") ?: return false
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).apply {
        val clip = ClipData.newUri(contentResolver, auxiliaryLabel, cachedImageUri)
        setPrimaryClip(clip)
    }
    return true
}

fun Context.showToast(
    @StringRes stringId: Int,
) {
    Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show()
}

fun Context.showToast(string: String) {
    Toast.makeText(this, getString(string), Toast.LENGTH_SHORT).show()
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
