package com.pedroid.qrcodecomposelib.common.internal

import com.google.zxing.BarcodeFormat
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import org.junit.Assert.assertEquals
import org.junit.Test

internal class QRCodeComposeXFormatMappersTest {
    @Test
    fun testToQRCodeComposeXFormat() {
        assertEquals(QRCodeComposeXFormat.QR_CODE, BarcodeFormat.QR_CODE.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.AZTEC, BarcodeFormat.AZTEC.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.DATA_MATRIX, BarcodeFormat.DATA_MATRIX.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.PDF_417, BarcodeFormat.PDF_417.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_39, BarcodeFormat.CODE_39.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_93, BarcodeFormat.CODE_93.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_128, BarcodeFormat.CODE_128.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8, BarcodeFormat.EAN_8.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13, BarcodeFormat.EAN_13.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_ITF, BarcodeFormat.ITF.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_US_UPC_A, BarcodeFormat.UPC_A.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_US_UPC_E, BarcodeFormat.UPC_E.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.CODABAR, BarcodeFormat.CODABAR.toQRCodeComposeXFormat())
    }

    @Test
    fun testNullMappings() {
        assertEquals(null, BarcodeFormat.MAXICODE.toQRCodeComposeXFormat())
        assertEquals(null, BarcodeFormat.RSS_14.toQRCodeComposeXFormat())
        assertEquals(null, BarcodeFormat.RSS_EXPANDED.toQRCodeComposeXFormat())
        assertEquals(null, BarcodeFormat.UPC_EAN_EXTENSION.toQRCodeComposeXFormat())
    }

    @Test
    fun testToZxingBarcodeFormat() {
        assertEquals(BarcodeFormat.QR_CODE, QRCodeComposeXFormat.QR_CODE.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.AZTEC, QRCodeComposeXFormat.AZTEC.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.DATA_MATRIX, QRCodeComposeXFormat.DATA_MATRIX.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.PDF_417, QRCodeComposeXFormat.PDF_417.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.CODE_39, QRCodeComposeXFormat.BARCODE_39.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.CODE_93, QRCodeComposeXFormat.BARCODE_93.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.CODE_128, QRCodeComposeXFormat.BARCODE_128.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.EAN_8, QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.EAN_13, QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.ITF, QRCodeComposeXFormat.BARCODE_ITF.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.UPC_A, QRCodeComposeXFormat.BARCODE_US_UPC_A.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.UPC_E, QRCodeComposeXFormat.BARCODE_US_UPC_E.toZxingBarcodeFormat())
        assertEquals(BarcodeFormat.CODABAR, QRCodeComposeXFormat.CODABAR.toZxingBarcodeFormat())
    }
}
