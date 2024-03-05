package com.pedroid.qrcodecompose.androidapp.core.presentation

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import java.io.OutputStream

const val IMAGE_MIME_TYPE = "image/png"
private const val IMAGE_FILE_EXTENSION = ".png"
private const val CACHED_IMAGES_DIRECTORY = "images"

fun Uri?.saveBitmap(
    context: Context,
    bitmap: Bitmap?,
): Boolean {
    return if (this == null || bitmap == null) {
        false
    } else {
        this.saveFile(context) {
            bitmap.compressToStream(it)
        }
    }
}

fun Context.saveImageInCache(
    imageBitmap: Bitmap?,
    fileNameNoExtension: String,
): Uri? {
    return if (imageBitmap == null || fileNameNoExtension.isBlank()) {
        null
    } else {
        return saveInCache(
            directoryName = CACHED_IMAGES_DIRECTORY,
            fileName = "$fileNameNoExtension$IMAGE_FILE_EXTENSION",
            clearFolderBeforeWriting = true,
            streamWriterDelegate = { imageBitmap.compressToStream(it) },
        )
    }
}

fun Bitmap.compressToStream(stream: OutputStream): Boolean = compress(Bitmap.CompressFormat.PNG, 90, stream)
