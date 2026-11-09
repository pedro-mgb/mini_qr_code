package com.pedroid.qrcodecomposelibmicroqr

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.camera.core.ImageProxy
import com.pedroid.qrcodecomposelib.scan.QRCodeCameraAnalyzer
import com.pedroid.qrcodecomposelib.scan.QRCodeScanResult
import zxingcpp.BarcodeReader
import java.util.concurrent.RejectedExecutionException
import java.util.concurrent.atomic.AtomicBoolean

private const val LOG_TAG = "MicroQRAnalyzer"

private const val DEFAULT_FRAME_INTERVAL = 3

/**
 * Camera analyzer that only detects Micro QR Codes, using zxing-cpp. Meant to complement an analyzer
 * that can't read them (e.g. ML Kit), not to replace it.
 *
 * To keep the cost low on slower devices:
 * - Only the Micro QR format is requested, so zxing-cpp doesn't instantiate the other readers (1D, Aztec, etc.)
 *      and skips the regular QR Code detection. All the "try harder" options are kept off.
 * - Only one in every [frameInterval] frames is decoded. Micro QR Codes are rare, and users keep the camera
 *      on a code for a while, so skipping frames costs very little in scan speed.
 * - Decoding is done on its own thread (not the one the frames are delivered on), and if the previous frame is
 *      still being decoded, the new one is dropped instead of queued.
 *
 * Every frame is closed - immediately if skipped, after decoding otherwise.
 *
 * [onQRCodeStatus] is always called on the main thread, same as with the other analyzers.
 *
 * Micro QR Codes are reported with the QR Code format, as it has no format of its own (yet).
 *
 * @param onQRCodeStatus listener for the result of each decoded frame
 * @param frameInterval how many frames to go through for each one that is decoded. 1 decodes every frame
 */
class MicroQRCameraAnalyzer(
    override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
    private val frameInterval: Int = DEFAULT_FRAME_INTERVAL,
) : QRCodeCameraAnalyzer {
    // lazy so the native library is loaded on the decoding thread, not when the screen is composed
    private val reader by lazy {
        BarcodeReader(BarcodeReader.Options(formats = setOf(BarcodeReader.Format.MICRO_QR_CODE)))
    }

    private val decodeExecutor = newDecodeExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val decoding = AtomicBoolean(false)

    // only accessed from the thread that frames are delivered on; starts due so the first frame is decoded
    private var framesSinceDecode: Int

    init {
        require(frameInterval >= 1) { "frameInterval must be at least 1, but was $frameInterval" }
        framesSinceDecode = frameInterval - 1
    }

    override fun analyze(image: ImageProxy) {
        if (++framesSinceDecode < frameInterval || !decoding.compareAndSet(false, true)) {
            // either not due yet, or still busy with a previous frame (in which case it's due again on the next frame)
            image.close()
            return
        }
        framesSinceDecode = 0
        try {
            decodeExecutor.execute { decode(image) }
        } catch (e: RejectedExecutionException) {
            Log.w(LOG_TAG, "Could not schedule decoding", e)
            decoding.set(false)
            image.close()
        }
    }

    private fun decode(image: ImageProxy) {
        val status =
            try {
                reader.read(image).firstOrNull()?.toQRCodeScanResult() ?: QRCodeScanResult.Invalid
            } catch (e: RuntimeException) {
                // e.g. image in a YUV format the reader doesn't support, or a native error
                Log.w(LOG_TAG, "Obtained error processing image for micro qr code", e)
                QRCodeScanResult.Invalid
            } finally {
                image.close()
                decoding.set(false)
            }
        mainHandler.post { onQRCodeStatus(status) }
    }
}
