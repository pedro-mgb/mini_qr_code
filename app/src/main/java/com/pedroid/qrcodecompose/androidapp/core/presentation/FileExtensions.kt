package com.pedroid.qrcodecompose.androidapp.core.presentation

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

private const val FILES_AUTHORITY = "com.pedroid.qrcodecompose.androidapp.files"

/**
 * Extension function to save a file in cache
 */
fun Context.saveInCache(
    directoryName: String,
    fileName: String,
    clearFolderBeforeWriting: Boolean,
    streamWriterDelegate: (OutputStream) -> Unit,
): Uri? {
    return try {
        val dir = File(cacheDir, directoryName)
        dir.mkdirs()
        if (clearFolderBeforeWriting) {
            dir.deleteSubFiles()
        }
        val newFile = File(dir, fileName)
        FileOutputStream(File(dir, fileName)).use {
            streamWriterDelegate(it)
            FileProvider.getUriForFile(this, FILES_AUTHORITY, newFile)
        }
    } catch (ex: IOException) {
        null
    }
}

/**
 * Extension function to save file in uri
 */
fun Uri.saveFile(
    context: Context,
    streamWriterDelegate: (OutputStream) -> Boolean,
): Boolean {
    return context.contentResolver.openOutputStream(this)?.use {
        streamWriterDelegate(it)
    } ?: false
}

private fun File.deleteSubFiles() {
    listFiles()?.forEach {
        if (!it.isDirectory) {
            it.delete()
        }
    }
}
