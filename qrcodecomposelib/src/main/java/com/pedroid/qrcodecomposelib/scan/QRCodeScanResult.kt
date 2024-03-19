package com.pedroid.qrcodecomposelib.scan

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat

sealed class QRCodeScanResult {
    data object Idle : QRCodeScanResult()

    data class Scanned(val qrCode: String, val format: QRCodeComposeXFormat) : QRCodeScanResult()

    data object Invalid : QRCodeScanResult()

    data object Cancelled : QRCodeScanResult()

    data object UnrecoverableError : QRCodeScanResult()
}
