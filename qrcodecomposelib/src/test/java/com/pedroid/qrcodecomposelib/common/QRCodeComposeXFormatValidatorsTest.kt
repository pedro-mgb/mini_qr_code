package com.pedroid.qrcodecomposelib.common

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class QRCodeComposeXFormatValidatorsTest {
    @Test
    fun `data matrix supports characters in ISO IEC 8859-1 format`() {
        val textWithISOcharsOnly = "123 Asz çé !%£"

        val result = QRCodeComposeXFormat.DATA_MATRIX.validationRegex.matches(textWithISOcharsOnly)

        assertTrue(result)
    }

    @Test
    fun `data matrix does not support characters that are not in ISO IEC 8859-1 format`() {
        val textWithNonIsoContent = "Привет \uD83D\uDE00"

        val result = QRCodeComposeXFormat.DATA_MATRIX.validationRegex.matches(textWithNonIsoContent)

        assertFalse(result)
    }

    @Test
    fun `barcode 39 supports ASCII characters`() {
        val textWithASCIIOnly = "123 Asz"

        val result = QRCodeComposeXFormat.BARCODE_39.validationRegex.matches(textWithASCIIOnly)

        assertTrue(result)
    }

    @Test
    fun `barcode 39 does not support non-ASCII characters`() {
        val textWithNonASCIIContent = "éáx 42"

        val result = QRCodeComposeXFormat.BARCODE_39.validationRegex.matches(textWithNonASCIIContent)

        assertFalse(result)
    }

    @Test
    fun `barcode 93 supported characters`() {
        val textWithSupported93 = "123 ZYX ABCD $-.+/% "

        val result = QRCodeComposeXFormat.BARCODE_93.validationRegex.matches(textWithSupported93)

        assertTrue(result)
    }

    @Test
    fun `barcode 93 does not support NON-ASCII characters and even some ASCII specific characters`() {
        val textWithoutSupported93 = "abcd :; é"

        val result = QRCodeComposeXFormat.BARCODE_93.validationRegex.matches(textWithoutSupported93)

        assertFalse(result)
    }

    @Test
    fun `barcode 128 supports ASCII characters`() {
        val textWithASCIIOnly = "123Asz"

        val result = QRCodeComposeXFormat.BARCODE_128.validationRegex.matches(textWithASCIIOnly)

        assertTrue(result)
    }

    @Test
    fun `barcode 128 does not support non-ASCII characters`() {
        val textWithNonASCIIContent = "éáx42"

        val result = QRCodeComposeXFormat.BARCODE_128.validationRegex.matches(textWithNonASCIIContent)

        assertFalse(result)
    }

    @Test
    fun `barcode 39, 93 and 128 does not support more than 80 characters`() {
        val numberMoreThan80 = "9".repeat(99)

        assertFalse(QRCodeComposeXFormat.BARCODE_39.validationRegex.matches(numberMoreThan80))
        assertFalse(QRCodeComposeXFormat.BARCODE_93.validationRegex.matches(numberMoreThan80))
        assertFalse(QRCodeComposeXFormat.BARCODE_128.validationRegex.matches(numberMoreThan80))
    }

    @Test
    fun `EAN 8 and UPC E allow numbers of either length 7 or 8`() {
        val numberLength7 = "1234567"
        val numberLength8 = "12345678"

        assertTrue(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.validationRegex.matches(numberLength7))
        assertTrue(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.validationRegex.matches(numberLength8))
        assertTrue(QRCodeComposeXFormat.BARCODE_US_UPC_E.validationRegex.matches(numberLength7))
        assertTrue(QRCodeComposeXFormat.BARCODE_US_UPC_E.validationRegex.matches(numberLength8))
    }

    @Test
    fun `EAN 8 and UPC E do not allow number with length less than 7`() {
        val numberLength6 = "123456"

        assertFalse(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.validationRegex.matches(numberLength6))
        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_E.validationRegex.matches(numberLength6))
    }

    @Test
    fun `EAN 8 and UPC E do not allow number with length larger than 8`() {
        val numberLength9 = "123459"

        assertFalse(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.validationRegex.matches(numberLength9))
        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_E.validationRegex.matches(numberLength9))
    }

    @Test
    fun `EAN 8 and UPC E do not allow characters other than numbers`() {
        val invalid = "abcdefg"

        assertFalse(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_8.validationRegex.matches(invalid))
        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_E.validationRegex.matches(invalid))
    }

    @Test
    fun `EAN 13 and UPC A allow numbers of either length 12 or 13`() {
        val numberLength12 = "0".repeat(12)
        val numberLength13 = "0".repeat(13)
        // 1 less for UPC-A because ZXING prepends a 0
        val numberLength11 = "1".repeat(11)

        assertTrue(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.validationRegex.matches(numberLength12))
        assertTrue(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.validationRegex.matches(numberLength13))
        assertTrue(QRCodeComposeXFormat.BARCODE_US_UPC_A.validationRegex.matches(numberLength11))
        assertTrue(QRCodeComposeXFormat.BARCODE_US_UPC_A.validationRegex.matches(numberLength12))
    }

    @Test
    fun `EAN 13 and UPC A do not allow number with length less than 12`() {
        val numberLength11 = "1".repeat(11)
        // 1 less for UPC-A because ZXING prepends a 0
        val numberLength10 = "1".repeat(10)

        assertFalse(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.validationRegex.matches(numberLength11))
        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_A.validationRegex.matches(numberLength10))
    }

    @Test
    fun `EAN 13 and UPC A do not allow number with length larger than 13`() {
        val numberLength14 = "1".repeat(14)

        assertFalse(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.validationRegex.matches(numberLength14))
        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_A.validationRegex.matches(numberLength14))
    }

    @Test
    fun `EAN 13 and UPC A do not allow characters other than numbers`() {
        val invalid = "abcdefg"

        assertFalse(QRCodeComposeXFormat.BARCODE_EUROPE_EAN_13.validationRegex.matches(invalid))
        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_A.validationRegex.matches(invalid))
    }

    @Test
    fun `UPC E cannot start with a number other than 1 or 0`() {
        val invalid = "8".repeat(7)

        assertFalse(QRCodeComposeXFormat.BARCODE_US_UPC_E.validationRegex.matches(invalid))
    }

    @Test
    fun `ITF supports an even amount of digits`() {
        val digitsEven = "8".repeat(80)

        assertTrue(QRCodeComposeXFormat.BARCODE_ITF.validationRegex.matches(digitsEven))
    }

    @Test
    fun `ITF does not support an odd amount of digits`() {
        val digitsOdd = "7".repeat(7)

        assertFalse(QRCodeComposeXFormat.BARCODE_ITF.validationRegex.matches(digitsOdd))
    }

    @Test
    fun `ITF does not support more than 80 digits`() {
        val digitsMore80 = "9".repeat(99)

        assertFalse(QRCodeComposeXFormat.BARCODE_ITF.validationRegex.matches(digitsMore80))
    }

    @Test
    fun `ITF does not support characters that are not digits`() {
        val alphaString = "a".repeat(4)

        assertFalse(QRCodeComposeXFormat.BARCODE_ITF.validationRegex.matches(alphaString))
    }

    @Test
    fun `Codabar supports digits`() {
        val digitsOdd = "7".repeat(7)

        assertTrue(QRCodeComposeXFormat.CODABAR.validationRegex.matches(digitsOdd))
    }

    @Test
    fun `Codabar does not support characters digits`() {
        val alphaString = "a".repeat(4)

        assertFalse(QRCodeComposeXFormat.CODABAR.validationRegex.matches(alphaString))
    }
}
