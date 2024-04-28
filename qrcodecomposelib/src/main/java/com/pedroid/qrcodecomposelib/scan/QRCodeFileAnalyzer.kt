package com.pedroid.qrcodecomposelib.scan

import android.content.Context
import android.net.Uri

/**
 * Interface responsible for handling analysis of file images and detecting QR Codes and barcodes
 */
interface QRCodeFileAnalyzer : QRCodeAnalyzerWithResult {
    fun analyze(
        context: Context,
        uri: Uri,
    )
}
