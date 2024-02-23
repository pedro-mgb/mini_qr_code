package com.pedroid.qrcodecompose.androidapp.core.logging

interface Logger {
    fun verbose(tag: String, message: String, t: Throwable? = null)

    fun debug(tag: String, message: String, t: Throwable? = null)

    fun info(tag: String, message: String, t: Throwable? = null)

    fun warning(tag: String, message: String, t: Throwable? = null)

    fun error(tag: String, message: String, t: Throwable? = null)
}