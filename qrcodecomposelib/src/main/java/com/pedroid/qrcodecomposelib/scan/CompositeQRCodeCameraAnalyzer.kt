package com.pedroid.qrcodecomposelib.scan

import android.util.Log
import androidx.camera.core.ImageProxy
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

private const val LOG_TAG = "CompositeAnalyzer"

/**
 * Creates an analyzer that reports its results to the given listener.
 * Needed because analyzers receive their listener when they are created.
 */
typealias QRCodeCameraAnalyzerFactory = (onResult: (QRCodeScanResult) -> Unit) -> QRCodeCameraAnalyzer

/**
 * Analyzer that runs several analyzers (engines) on the same camera frames.
 *
 * - The [primary] analyzer's results are all forwarded, including [QRCodeScanResult.Invalid] when nothing was found.
 * - Results of the [secondary] analyzers are only forwarded when they did find a code ([QRCodeScanResult.Scanned]).
 *      Otherwise a secondary analyzer that skips frames, or only supports a few formats, would make the
 *      listener believe that there was nothing in a frame that the primary analyzer did read.
 *
 * Meant for analyzers that support different formats. Does not remove duplicates, so if several analyzers
 * can read the same code, the listener is notified once for each of them.
 *
 * Each frame is shared between all the analyzers, and only closed once all of them are done with it.
 * Analyzers may close it at different times, and on different threads.
 * Therefore the listener may also be called from different threads, depending on the analyzers.
 *
 * @param onQRCodeStatus listener for the results
 * @param primary the analyzer that decides if a frame had no code
 * @param secondary analyzers that only add to what the primary one is able to find
 */
class CompositeQRCodeCameraAnalyzer(
    override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
    primary: QRCodeCameraAnalyzerFactory,
    secondary: List<QRCodeCameraAnalyzerFactory> = emptyList(),
) : QRCodeCameraAnalyzer {
    private val analyzers: List<QRCodeCameraAnalyzer> =
        listOf(primary(onQRCodeStatus)) +
            secondary.map { factory ->
                factory { status ->
                    if (status is QRCodeScanResult.Scanned) {
                        onQRCodeStatus(status)
                    }
                }
            }

    override fun analyze(image: ImageProxy) {
        val sharedImage = SharedImage(image, handles = analyzers.size)
        analyzers.forEach { analyzer ->
            val handle = sharedImage.newHandle()
            try {
                analyzer.analyze(handle)
            } catch (e: RuntimeException) {
                // otherwise the frame would never be released, and the camera would stop delivering new ones
                Log.e(LOG_TAG, "Analyzer $analyzer failed", e)
                handle.close()
            }
        }
    }
}

/**
 * Wraps an image to be used by several consumers at once: each gets its own handle,
 * and the image is only closed when all handles have been (and closing a handle more than once has no effect).
 */
private class SharedImage(
    // not named "image", as inside Handle that would be the image property inherited from ImageProxy
    private val source: ImageProxy,
    handles: Int,
) {
    private val openHandles = AtomicInteger(handles)

    fun newHandle(): ImageProxy = Handle()

    private inner class Handle : ImageProxy by source {
        private val closed = AtomicBoolean(false)

        override fun close() {
            if (closed.compareAndSet(false, true) && openHandles.decrementAndGet() == 0) {
                source.close()
            }
        }
    }
}
