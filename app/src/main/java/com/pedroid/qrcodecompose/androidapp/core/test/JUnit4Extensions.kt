package com.pedroid.qrcodecompose.androidapp.core.test

import org.junit.Assert.fail

fun assertContains(
    fullString: String,
    expectedToContain: String,
) {
    if (!fullString.contains(expectedToContain)) {
        fail("\"$fullString\" does not contain \"$expectedToContain\"")
    }
}
