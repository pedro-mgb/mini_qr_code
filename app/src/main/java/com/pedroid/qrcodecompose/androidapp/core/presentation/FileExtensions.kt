package com.pedroid.qrcodecompose.androidapp.core.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri

const val IMAGE_MIME_TYPE = "image/png"

/**
 * Extension function to save bitmap in uri
 */
fun Uri?.saveBitmap(
    context: Context,
    bitmap: Bitmap?,
): Boolean {
    return if (this == null || bitmap == null) {
        false
    } else {
        context.contentResolver.openOutputStream(this)?.use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, it)
        } ?: false
    }
}
