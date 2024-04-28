package com.pedroid.qrcodecomposelib.scan

import androidx.camera.core.ImageAnalysis

/**
 * Interface responsible for handling analysis of camera images and detecting QR Codes and barcodes
 */
interface QRCodeCameraAnalyzer : QRCodeAnalyzerWithResult, ImageAnalysis.Analyzer
