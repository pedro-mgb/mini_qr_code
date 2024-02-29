package com.pedroid.qrcodecompose.androidapp.core.presentation

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri

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

fun Context.copyTextToClipboard(
    text: String,
    auxiliaryLabel: String = "",
) {
    (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).copyText(text, auxiliaryLabel)
}

fun ClipboardManager.copyText(
    text: String,
    auxiliaryLabel: String = "",
) {
    val clip = ClipData.newPlainText(auxiliaryLabel, text)
    setPrimaryClip(clip)
}

@SuppressLint("DiscouragedApi")
fun Context.getString(key: String): String? {
    val resId: Int = resources.getIdentifier(key, "string", packageName)
    return if (resId > 0) {
        try {
            getString(resId)
        } catch (e: Resources.NotFoundException) {
            null
        }
    } else {
        null
    }
}
