package com.pedroid.qrcodecomposelibmicroqr

import org.junit.Assert.assertEquals
import org.junit.Test

internal class MicroQRFileAnalyzerTest {
    @Test
    fun `small images are not scaled`() {
        assertEquals(1, calculateSampleSize(width = 1000, height = 800))
        assertEquals(1, calculateSampleSize(width = MAX_IMAGE_SIDE, height = 100))
    }

    @Test
    fun `larger side is what counts, regardless of orientation`() {
        assertEquals(2, calculateSampleSize(width = 4000, height = 3000))
        assertEquals(2, calculateSampleSize(width = 3000, height = 4000))
    }

    @Test
    fun `scales by the smallest power of 2 that fits`() {
        assertEquals(1, calculateSampleSize(width = MAX_IMAGE_SIDE, height = MAX_IMAGE_SIDE))
        assertEquals(2, calculateSampleSize(width = MAX_IMAGE_SIDE + 1, height = 100))
        assertEquals(2, calculateSampleSize(width = 4608, height = 3456))
        assertEquals(4, calculateSampleSize(width = 12000, height = 9000))
    }

    @Test
    fun `respects the given max side`() {
        assertEquals(4, calculateSampleSize(width = 4000, height = 3000, maxSide = 1000))
    }
}
