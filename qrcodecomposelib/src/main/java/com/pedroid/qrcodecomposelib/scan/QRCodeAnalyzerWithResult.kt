package com.pedroid.qrcodecomposelib.scan

interface QRCodeAnalyzerWithResult {
    val onQRCodeStatus: (QRCodeScanResult) -> Unit
}
