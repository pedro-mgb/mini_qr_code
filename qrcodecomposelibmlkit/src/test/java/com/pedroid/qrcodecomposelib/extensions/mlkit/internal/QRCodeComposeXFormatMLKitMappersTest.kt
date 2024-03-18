package com.pedroid.qrcodecomposelib.extensions.mlkit.internal

import com.google.mlkit.vision.barcode.common.Barcode
import com.pedroid.qrcodecomposelib.common.QRCodeComposeXFormat
import com.pedroid.qrcodecomposelibmlkit.internal.toMLKitBarcodeFormat
import com.pedroid.qrcodecomposelibmlkit.internal.toQRCodeComposeXFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class QRCodeComposeXFormatMLKitMappersTest {
    @Test
    fun testToQRCodeComposeXFormat() {
        assertEquals(QRCodeComposeXFormat.QR_CODE, Barcode.FORMAT_QR_CODE.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.AZTEC, Barcode.FORMAT_AZTEC.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.DATA_MATRIX, Barcode.FORMAT_DATA_MATRIX.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.PDF_417, Barcode.FORMAT_PDF417.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_39, Barcode.FORMAT_CODE_39.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_93, Barcode.FORMAT_CODE_93.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_128, Barcode.FORMAT_CODE_128.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8, Barcode.FORMAT_EAN_8.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13, Barcode.FORMAT_EAN_13.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_ITF, Barcode.FORMAT_ITF.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_US_UPC_A, Barcode.FORMAT_UPC_A.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.BARCODE_US_UPC_E, Barcode.FORMAT_UPC_E.toQRCodeComposeXFormat())
        assertEquals(QRCodeComposeXFormat.CODABAR, Barcode.FORMAT_CODABAR.toQRCodeComposeXFormat())
    }

    @Test
    fun testNullMappings() {
        assertEquals(null, Barcode.FORMAT_ALL_FORMATS.toQRCodeComposeXFormat())
        assertEquals(null, Barcode.FORMAT_UNKNOWN.toQRCodeComposeXFormat())
    }

    @Test
    fun testToMLKitBarcodeFormat() {
        assertEquals(Barcode.FORMAT_QR_CODE, QRCodeComposeXFormat.QR_CODE.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_AZTEC, QRCodeComposeXFormat.AZTEC.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_DATA_MATRIX, QRCodeComposeXFormat.DATA_MATRIX.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_PDF417, QRCodeComposeXFormat.PDF_417.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_CODE_39, QRCodeComposeXFormat.BARCODE_39.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_CODE_93, QRCodeComposeXFormat.BARCODE_93.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_CODE_128, QRCodeComposeXFormat.BARCODE_128.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_EAN_8, QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_EAN_13, QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_ITF, QRCodeComposeXFormat.BARCODE_ITF.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_UPC_A, QRCodeComposeXFormat.BARCODE_US_UPC_A.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_UPC_E, QRCodeComposeXFormat.BARCODE_US_UPC_E.toMLKitBarcodeFormat())
        assertEquals(Barcode.FORMAT_CODABAR, QRCodeComposeXFormat.CODABAR.toMLKitBarcodeFormat())
    }
}
