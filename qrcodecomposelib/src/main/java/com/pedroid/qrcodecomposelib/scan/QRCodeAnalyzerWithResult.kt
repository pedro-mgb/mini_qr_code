package com.pedroid.qrcodecomposelib.scan

/**
 * Interface for providing result of analysis/detection of QR Codes and barcodes
 */
interface QRCodeAnalyzerWithResult {
    val onQRCodeStatus: (QRCodeScanResult) -> Unit
}
