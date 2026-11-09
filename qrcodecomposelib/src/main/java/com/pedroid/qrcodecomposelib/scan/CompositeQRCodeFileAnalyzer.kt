package com.pedroid.qrcodecomposelib.scan

import android.content.Context
import android.net.Uri

/**
 * Creates an analyzer that reports its result to the given listener.
 * Needed because analyzers receive their listener when they are created.
 */
typealias QRCodeFileAnalyzerFactory = (onResult: (QRCodeScanResult) -> Unit) -> QRCodeFileAnalyzer

/**
 * File analyzer that tries several analyzers (engines) one after the other, until one finds a code.
 *
 * The [primary] analyzer is always the first to analyze the file. Only if it reports [QRCodeScanResult.Invalid]
 * (no code found) does the next one, from [fallbacks], analyze the same file, and so on.
 * Any other result is final, including errors - there is no point in trying another analyzer if the file can't be read.
 * If none of the analyzers finds a code, the result is [QRCodeScanResult.Invalid].
 *
 * Meant for analyzers that support different formats. As a file is only analyzed once, there is no need to run them
 * at the same time, and the fallbacks only have a cost when the primary analyzer didn't find anything.
 *
 * New analyzers are created for each file, to not have state shared between files that are analyzed at the same time.
 * Fallback analyzers are only created if needed.
 *
 * @param onQRCodeStatus listener for the result
 * @param primary the analyzer that is always used
 * @param fallbacks analyzers that are used in order, if the previous ones did not find a code
 */
class CompositeQRCodeFileAnalyzer(
    override val onQRCodeStatus: (QRCodeScanResult) -> Unit,
    primary: QRCodeFileAnalyzerFactory,
    fallbacks: List<QRCodeFileAnalyzerFactory> = emptyList(),
) : QRCodeFileAnalyzer {
    private val factories: List<QRCodeFileAnalyzerFactory> = listOf(primary) + fallbacks

    override fun analyze(
        context: Context,
        uri: Uri,
    ) {
        analyze(context, uri, stage = 0)
    }

    private fun analyze(
        context: Context,
        uri: Uri,
        stage: Int,
    ) {
        val isLastStage = stage == factories.lastIndex
        factories[stage] { status ->
            if (status is QRCodeScanResult.Invalid && !isLastStage) {
                analyze(context, uri, stage + 1)
            } else {
                onQRCodeStatus(status)
            }
        }.analyze(context, uri)
    }
}
