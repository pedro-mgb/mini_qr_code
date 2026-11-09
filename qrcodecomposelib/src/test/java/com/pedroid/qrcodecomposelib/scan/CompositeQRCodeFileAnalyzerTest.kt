package com.pedroid.qrcodecomposelib.scan

import android.content.Context
import android.net.Uri
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CompositeQRCodeFileAnalyzerTest {
    private val context = mockk<Context>()
    private val uri = mockk<Uri>()

    private val results = mutableListOf<QRCodeScanResult>()
    private val createdAnalyzers = mutableListOf<FakeAnalyzer>()

    @Test
    fun `result of primary analyzer is final if a code was found`() {
        val composite = createComposite(SCANNED, fallbackResults = listOf(OTHER_SCANNED))

        composite.analyze(context, uri)

        assertEquals(listOf(SCANNED), results)
        assertEquals(1, createdAnalyzers.size)
    }

    @Test
    fun `errors and cancellations of primary analyzer are final`() {
        listOf(QRCodeScanResult.UnrecoverableError, QRCodeScanResult.Cancelled).forEach { result ->
            results.clear()
            createdAnalyzers.clear()

            createComposite(result, fallbackResults = listOf(OTHER_SCANNED)).analyze(context, uri)

            assertEquals(listOf(result), results)
            assertEquals(1, createdAnalyzers.size)
        }
    }

    @Test
    fun `fallback analyzes the same file when primary does not find a code`() {
        val composite = createComposite(QRCodeScanResult.Invalid, fallbackResults = listOf(OTHER_SCANNED))

        composite.analyze(context, uri)

        assertEquals(listOf(OTHER_SCANNED), results)
        assertEquals(listOf(context to uri), createdAnalyzers[0].analyzed)
        assertEquals(listOf(context to uri), createdAnalyzers[1].analyzed)
    }

    @Test
    fun `result is invalid if no analyzer finds a code`() {
        val composite =
            createComposite(
                QRCodeScanResult.Invalid,
                fallbackResults = listOf(QRCodeScanResult.Invalid, QRCodeScanResult.Invalid),
            )

        composite.analyze(context, uri)

        assertEquals(listOf(QRCodeScanResult.Invalid), results)
        assertEquals(3, createdAnalyzers.size)
    }

    @Test
    fun `fallbacks are used in order until a code is found`() {
        val composite =
            createComposite(
                QRCodeScanResult.Invalid,
                fallbackResults = listOf(QRCodeScanResult.Invalid, SCANNED, OTHER_SCANNED),
            )

        composite.analyze(context, uri)

        assertEquals(listOf(SCANNED), results)
        assertEquals(3, createdAnalyzers.size)
    }

    @Test
    fun `fallback errors are final`() {
        val composite =
            createComposite(
                QRCodeScanResult.Invalid,
                fallbackResults = listOf(QRCodeScanResult.UnrecoverableError, SCANNED),
            )

        composite.analyze(context, uri)

        assertEquals(listOf(QRCodeScanResult.UnrecoverableError), results)
        assertEquals(2, createdAnalyzers.size)
    }

    @Test
    fun `works with only a primary analyzer`() {
        val composite = createComposite(QRCodeScanResult.Invalid)

        composite.analyze(context, uri)

        assertEquals(listOf(QRCodeScanResult.Invalid), results)
    }

    @Test
    fun `each file is analyzed independently`() {
        val composite = createComposite(QRCodeScanResult.Invalid, fallbackResults = listOf(SCANNED))

        composite.analyze(context, uri)
        composite.analyze(context, uri)

        assertEquals(listOf(SCANNED, SCANNED), results)
        assertEquals(4, createdAnalyzers.size)
    }

    private fun createComposite(
        primaryResult: QRCodeScanResult,
        fallbackResults: List<QRCodeScanResult> = emptyList(),
    ) = CompositeQRCodeFileAnalyzer(
        onQRCodeStatus = { results += it },
        primary = { FakeAnalyzer(it, primaryResult).also { analyzer -> createdAnalyzers += analyzer } },
        fallbacks =
            fallbackResults.map { result ->
                { onResult -> FakeAnalyzer(onResult, result).also { analyzer -> createdAnalyzers += analyzer } }
            },
    )

    private class FakeAnalyzer(
        override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
        private val result: QRCodeScanResult,
    ) : QRCodeFileAnalyzer {
        val analyzed = mutableListOf<Pair<Context, Uri>>()

        override fun analyze(
            context: Context,
            uri: Uri,
        ) {
            analyzed += context to uri
            onQRCodeStatus(result)
        }
    }

    private companion object {
        val SCANNED = QRCodeScanResult.Scanned("1234", QRCodeComposeXFormat.QR_CODE)
        val OTHER_SCANNED = QRCodeScanResult.Scanned("5678", QRCodeComposeXFormat.QR_CODE)
    }
}
