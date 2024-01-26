package com.pedroid.qrcode_compose_x.scan

sealed interface QRCodeScanResult {
    data object Ready : QRCodeScanResult

    data class Scanned(val qrCode: String) : QRCodeScanResult

    data object Invalid : QRCodeScanResult

    data object UnrecoverableError : QRCodeScanResult
}