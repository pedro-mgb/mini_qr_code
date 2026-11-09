package com.pedroid.qrcodecomposelib.scan

import android.util.Log
import androidx.camera.core.ImageProxy
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

internal class CompositeQRCodeCameraAnalyzerTest {
    private val image = mockk<ImageProxy>(relaxed = true)
    private val results = mutableListOf<QRCodeScanResult>()

    private lateinit var primary: FakeAnalyzer
    private lateinit var secondary: FakeAnalyzer

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `frame is only closed once all analyzers are done with it`() {
        val composite = createComposite()
        val handles = mutableListOf<ImageProxy>()
        primary.onAnalyze = { handles += it }
        secondary.onAnalyze = { handles += it }

        composite.analyze(image)
        handles[0].close()
        verify(exactly = 0) { image.close() }
        handles[1].close()
        verify(exactly = 1) { image.close() }
    }

    @Test
    fun `closing a handle more than once does not close the frame early`() {
        val composite = createComposite()
        val handles = mutableListOf<ImageProxy>()
        primary.onAnalyze = { handles += it }
        secondary.onAnalyze = { handles += it }

        composite.analyze(image)
        handles[0].close()
        handles[0].close()
        verify(exactly = 0) { image.close() }
    }

    @Test
    fun `frame is closed if analyzers close it right away`() {
        val composite = createComposite()
        primary.onAnalyze = { it.close() }
        secondary.onAnalyze = { it.close() }

        composite.analyze(image)
        verify(exactly = 1) { image.close() }
    }

    @Test
    fun `frame is closed if an analyzer throws`() {
        val composite = createComposite()
        primary.onAnalyze = { it.close() }
        secondary.onAnalyze = { throw IllegalStateException("failed") }

        composite.analyze(image)
        verify(exactly = 1) { image.close() }
    }

    @Test
    fun `all results of primary analyzer are forwarded`() {
        createComposite()

        primary.onQRCodeStatus(QRCodeScanResult.Invalid)
        primary.onQRCodeStatus(SCANNED)
        primary.onQRCodeStatus(QRCodeScanResult.UnrecoverableError)

        assertEquals(listOf(QRCodeScanResult.Invalid, SCANNED, QRCodeScanResult.UnrecoverableError), results)
    }

    @Test
    fun `only scanned results of secondary analyzer are forwarded`() {
        createComposite()

        secondary.onQRCodeStatus(QRCodeScanResult.Invalid)
        secondary.onQRCodeStatus(QRCodeScanResult.Cancelled)
        secondary.onQRCodeStatus(QRCodeScanResult.UnrecoverableError)
        secondary.onQRCodeStatus(SCANNED)

        assertEquals(listOf(SCANNED), results)
    }

    @Test
    fun `works with only a primary analyzer`() {
        val composite =
            CompositeQRCodeCameraAnalyzer(
                onQRCodeStatus = { results += it },
                primary = { FakeAnalyzer(it).also { analyzer -> primary = analyzer } },
            )
        primary.onAnalyze = { it.close() }

        composite.analyze(image)
        primary.onQRCodeStatus(SCANNED)

        verify(exactly = 1) { image.close() }
        assertEquals(listOf(SCANNED), results)
    }

    private fun createComposite() =
        CompositeQRCodeCameraAnalyzer(
            onQRCodeStatus = { results += it },
            primary = { FakeAnalyzer(it).also { analyzer -> primary = analyzer } },
            secondary = listOf({ FakeAnalyzer(it).also { analyzer -> secondary = analyzer } }),
        )

    private class FakeAnalyzer(
        override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
    ) : QRCodeCameraAnalyzer {
        var onAnalyze: (ImageProxy) -> Unit = {}

        override fun analyze(image: ImageProxy) = onAnalyze(image)
    }

    private companion object {
        val SCANNED = QRCodeScanResult.Scanned("1234", QRCodeComposeXFormat.QR_CODE)
    }
}
