package com.pedroid.qrcodecompose.androidapp.features.settings.data

import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class QRCodeSettingsRepositoryTest {
    @get:Rule(order = 0)
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testScope = CoroutineScope(coroutineDispatcherTestRule.testDispatcher)

    /* instead of mocking datastore completely, we use a real implementation (protobuf is not android specific)
        in that sense, these tests are not entirely a unit test, but rather resembles more an integration test
        The contents are saved to a temporary file; credits:
            1. https://github.com/android/nowinandroid/blob/main/core/datastore-test/src/main/kotlin/com/google/samples/apps/nowinandroid/core/datastore/test/TestDataStoreModule.kt
            2. https://github.com/android/nowinandroid/blob/main/core/datastore/src/test/kotlin/com/google/samples/apps/nowinandroid/core/datastore/NiaPreferencesDataSourceTest.kt
     */
    private val dataStore by lazy {
        tmpFolder.testUserPreferencesDataStore(testScope)
    }

    private lateinit var sut: QRCodeSettingsRepository

    @Before
    fun setUp() {
        sut = QRCodeSettingsRepository(dataStore)
    }

    @Test
    fun `given initial empty datastore, returns FullSettings with default values`() =
        runTest(testScope.coroutineContext) {
            val result = sut.getFullSettings().first()
            assertEquals(FullSettings(), result)
        }
}
