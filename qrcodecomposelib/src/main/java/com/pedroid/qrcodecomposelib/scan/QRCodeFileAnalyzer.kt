package com.pedroid.qrcodecomposelib.scan

import android.content.Context
import android.net.Uri

interface QRCodeFileAnalyzer : QRCodeAnalyzerWithResult {
    fun analyze(
        context: Context,
        uri: Uri,
    )
}
