package com.pedroid.qrcodecompose.androidapp.core.logging

import android.util.Log
import javax.inject.Inject

class AndroidLogger
    @Inject
    constructor() : Logger {
        override fun verbose(
            tag: String,
            message: String,
            t: Throwable?,
        ) {
            Log.v(tag, message)
        }

        override fun debug(
            tag: String,
            message: String,
            t: Throwable?,
        ) {
            Log.d(tag, message)
        }

        override fun info(
            tag: String,
            message: String,
            t: Throwable?,
        ) {
            Log.i(tag, message)
        }

        override fun warning(
            tag: String,
            message: String,
            t: Throwable?,
        ) {
            Log.w(tag, message)
        }

        override fun error(
            tag: String,
            message: String,
            t: Throwable?,
        ) {
            Log.e(tag, message, t)
        }
    }
