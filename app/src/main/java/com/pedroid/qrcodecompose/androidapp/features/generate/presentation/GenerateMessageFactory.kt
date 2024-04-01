package com.pedroid.qrcodecompose.androidapp.features.generate.presentation

import android.annotation.SuppressLint
import android.content.Context
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Using this component to integrate in ViewModel, so that error message is just consumed by UI and not build by it,
 * as it's based on business rules
 */
class GenerateMessageFactory
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) {
        @SuppressLint("StringFormatInvalid")
        internal fun createGenerateErrorMessage(
            format: QRCodeComposeXFormat,
            inputText: String,
        ): String {
            return try {
                context.getString(format.errorMessageStringId, inputText.length)
            } catch (ex: IllegalArgumentException) {
                // string has no arguments, or some of the arguments was not properly formatted
                //  instead of crashing, just get string without arguments
                context.getString(format.errorMessageStringId)
            }
        }
    }
