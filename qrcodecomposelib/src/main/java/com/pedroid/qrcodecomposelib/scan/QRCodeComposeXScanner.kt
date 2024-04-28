package com.pedroid.qrcodecomposelib.scan

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.pedroid.qrcodecomposelib.scan.internal.ZxingAnalyzer
import com.pedroid.qrcodecomposelib.scan.internal.drawScannerFrame

/**
 * Composable function to show the camera and scan QR Codes and barcodes
 *
 * @param modifier a Compose Modifier
 * @param frameColor color of the frame inside the camera preview
 * @param onResult listener for the scan results - be it code scanned, or error.
 *                 The listener is notified each time a qr code is detected. If the same code is detected twice in different frames, the listener is called twice.
 * @param analyzer the type of analyzer engine to use. By default uses Zxing, but different ones can be specified
 * @param frameVerticalPercent percentage for how high or low should the frame be placed. By default it's 50% - middle of screen
 * @param androidContext Android context to get access to the camera
 * @param lifecycleOwner lifecycle owner to bind to the camera process
 */
@Composable
@RequiresPermission(android.Manifest.permission.CAMERA)
fun QRCodeComposeXScanner(
    modifier: Modifier = Modifier,
    frameColor: Color,
    onResult: (QRCodeScanResult) -> Unit,
    analyzer: QRCodeCameraAnalyzer = defaultQRCodeCameraAnalyzer(onResult),
    frameVerticalPercent: Float = 0.5f,
    androidContext: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val cameraProviderFuture =
        remember {
            ProcessCameraProvider.getInstance(androidContext)
        }
    Box(
        modifier = modifier,
    ) {
        AndroidView(
            modifier =
                Modifier
                    .matchParentSize(),
            factory = { context ->
                val cameraPreviewView = PreviewView(context)
                val preview = Preview.Builder().build()
                val selector = buildBackCameraSelector()
                preview.setSurfaceProvider(cameraPreviewView.surfaceProvider)
                val imageAnalysis = buildImageAnalysis()
                imageAnalysis.setAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    analyzer,
                )
                try {
                    cameraProviderFuture.get().bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        imageAnalysis,
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                cameraPreviewView
            },
        )
        Box(
            modifier =
                Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .drawScannerFrame(frameColor, frameVerticalPercent),
        )
    }
}

fun defaultQRCodeCameraAnalyzer(onResult: (QRCodeScanResult) -> Unit): QRCodeCameraAnalyzer = ZxingAnalyzer(onResult)

fun defaultQRCodeFileAnalyzer(onResult: (QRCodeScanResult) -> Unit): QRCodeFileAnalyzer = ZxingAnalyzer(onResult)

private fun buildBackCameraSelector() =
    CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

private fun buildImageAnalysis() =
    ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()
