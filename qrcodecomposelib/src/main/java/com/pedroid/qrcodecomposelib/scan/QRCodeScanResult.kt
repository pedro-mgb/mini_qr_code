package com.pedroid.qrcodecomposelib.scan

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

/**
 * Type of result passed regarding the Scanning of QR Codes and barcodes.
 *
 * Initially set to Idle.
 * If successful, Scanned is returned.
 * Can also be result of cancelled, or errors having occurred.
 */
sealed class QRCodeScanResult {
    data object Idle : QRCodeScanResult()

    data class Scanned(val qrCode: String, val format: QRCodeComposeXFormat) : QRCodeScanResult()

    data object Invalid : QRCodeScanResult()

    data object Cancelled : QRCodeScanResult()

    data object UnrecoverableError : QRCodeScanResult()
}
