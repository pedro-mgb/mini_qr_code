package com.pedroid.qrcodecomposelibmicroqr

import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import zxingcpp.BarcodeReader
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

private const val IDLE_THREAD_TIMEOUT_SECONDS = 5L

/**
 * A single thread to decode on. It goes away when idle, as there is no way to know when an analyzer is no longer needed.
 */
internal fun newDecodeExecutor(): ThreadPoolExecutor =
    ThreadPoolExecutor(1, 1, IDLE_THREAD_TIMEOUT_SECONDS, TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>())
        .apply { allowCoreThreadTimeOut(true) }

/**
 * Micro QR Codes are reported with [QRCodeComposeXFormat.QR_CODE] format, as it has no format of its own (yet).
 */
internal fun BarcodeReader.Result.toQRCodeScanResult(): QRCodeScanResult {
    val content = text
    return if (content.isNullOrEmpty()) {
        QRCodeScanResult.Invalid
    } else {
        QRCodeScanResult.Scanned(content, QRCodeComposeXFormat.QR_CODE)
    }
}
