package com.pedroid.qrcodecomposelib.scan

import androidx.camera.core.ImageAnalysis

interface QRCodeCameraAnalyzer : QRCodeAnalyzerWithResult, ImageAnalysis.Analyzer
