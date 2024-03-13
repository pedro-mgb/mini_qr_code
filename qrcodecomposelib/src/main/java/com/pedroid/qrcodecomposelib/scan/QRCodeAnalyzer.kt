package com.pedroid.qrcodecomposelib.scan

import androidx.camera.core.ImageAnalysis

interface QRCodeAnalyzer : ImageAnalysis.Analyzer {
    val onQRCodeStatus: (QRCodeScanResult) -> Unit
}
