package com.pedroid.qrcodecomposelib.scan

sealed interface QRCodeScanResult {
    data object Ready : QRCodeScanResult

    data class Scanned(val qrCode: String) : QRCodeScanResult

    data object Invalid : QRCodeScanResult

    data object UnrecoverableError : QRCodeScanResult
}
