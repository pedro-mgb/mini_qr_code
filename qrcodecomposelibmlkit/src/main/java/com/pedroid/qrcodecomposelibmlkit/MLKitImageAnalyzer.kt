package com.pedroid.qrcodecomposelibmlkit

import android.util.Log
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.pedroid.qrcodecomposelib.scan.QRCodeAnalyzer
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult

private const val LOG_TAG = "MLKitAnalyzer"

@ExperimentalGetImage
class MLKitImageAnalyzer(
    override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
) : QRCodeAnalyzer {

    private val scanningOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()

    private val barcodeScanner by lazy { BarcodeScanning.getClient(scanningOptions) }

    override fun analyze(image: ImageProxy) {
        Log.v(LOG_TAG, "analyzing image $image")
        image.image?.let { currentImage ->
            val inputImage =
                InputImage.fromMediaImage(currentImage, image.imageInfo.rotationDegrees)
            barcodeScanner.process(inputImage)
                .addListeners(image)
        }
    }

    private fun Task<List<Barcode>>.addListeners(imageProxy: ImageProxy): Task<List<Barcode>> =
        addOnSuccessListener { barcodes ->
            onCodeScanned(barcodes)
        }.addOnFailureListener { ex ->
            Log.e(LOG_TAG, "Obtained error processing image for qr code", ex)
            onQRCodeStatus(QRCodeScanResult.Invalid)
        }.addOnCanceledListener {
            Log.d(LOG_TAG, "Cancelled processing")
            onQRCodeStatus(QRCodeScanResult.Cancelled)
        }.addOnCompleteListener {
            Log.d(LOG_TAG, "Complete processing")
            imageProxy.close()
        }

    private fun onCodeScanned(barcodes: List<Barcode>) {
        Log.d(LOG_TAG, "scanned barcodes: $barcodes")
        if (barcodes.isNotEmpty()) {
            // prioritize QR Codes
            val barcodeReturn = barcodes.firstOrNull {
                it.format == Barcode.FORMAT_QR_CODE
            } ?: barcodes.first()
            Log.d(
                LOG_TAG,
                "returning barcode: $barcodeReturn, value=${barcodeReturn.rawValue}"
            )
            barcodeReturn.rawValue?.let { value ->
                onQRCodeStatus(QRCodeScanResult.Scanned(value))
            }
        } else {
            Log.v(LOG_TAG, "No barcodes found")
            onQRCodeStatus(QRCodeScanResult.Invalid)
        }
    }
}