package com.pedroid.qrcodecompose.androidapp.features.settings.data

import app.cash.turbine.test
import com.pedroid.qrcodecompose.androidapp.core.test.CoroutineDispatcherTestRule
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

/*
TODO make these unit tests work in windows
 fails with IOException Unable to rename .../user_preferences_test.pb.tmp to ...\user_preferences_test.pb.
 This likely means that there are multiple instances of DataStore for this file.
 Ensure that you are only creating a single instance of datastore for this file.
 According to https://stackoverflow.com/questions/75326207/android-unit-test-are-passing-on-mac-but-failing-on-windows
 should be fixed now, but getting the same error;
 NowInAndroid similat unit tests fail as well - https://github.com/android/nowinandroid/issues/98
 Relevant issue in issue tracker - https://issuetracker.google.com/issues/203087070
 Will have to wait until it is fixed...
 Alternatively, if no fix is available, we could make this test class as instrumented test instead of unit test.
 */
class QRCodeSettingsRepositoryTest {
    @get:Rule(order = 0)
    val coroutineDispatcherTestRule = CoroutineDispatcherTestRule()

    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    private val testScope = CoroutineScope(coroutineDispatcherTestRule.testDispatcher)

    private lateinit var sut: QRCodeSettingsRepository

    @Before
    fun setUp() {
        /* instead of mocking datastore completely, we use a real implementation (protobuf is not android specific)
            in that sense, these tests are not entirely a unit test, but rather resembles more an integration test
            The contents are saved to a temporary file; credits:
                1. https://github.com/android/nowinandroid/blob/main/core/datastore-test/src/main/kotlin/com/google/samples/apps/nowinandroid/core/datastore/test/TestDataStoreModule.kt
                2. https://github.com/android/nowinandroid/blob/main/core/datastore/src/test/kotlin/com/google/samples/apps/nowinandroid/core/datastore/NiaPreferencesDataSourceTest.kt
         */
        sut =
            QRCodeSettingsRepository(
                settingsDataStore = tmpFolder.testUserPreferencesDataStore(testScope),
            )
    }

    @Test
    fun `given initial empty datastore, returns FullSettings with default values`() =
        runTest(testScope.coroutineContext) {
            val result = sut.getFullSettings().first()
            assertEquals(FullSettings(), result)
        }

    @Test
    fun `given datastore with contents, returns FullSettings with the values`() =
        runTest(testScope.coroutineContext) {
            sut.setAppLanguage("en")
            sut.setOpenUrlPreferences(OpenUrlPreferences.IN_BROWSER)
            sut.setScanHapticFeedback(true)
            sut.setScanHistorySavePreferences(HistorySavePreferences.UPON_USER_ACTION)
            sut.setGenerateHistorySavePreferences(HistorySavePreferences.NEVER_SAVE)
            val expected =
                FullSettings(
                    general =
                        GeneralSettings(
                            language = "en",
                            openUrlPreferences = OpenUrlPreferences.IN_BROWSER,
                        ),
                    scan =
                        ScanSettings(
                            hapticFeedback = true,
                            historySave = HistorySavePreferences.UPON_USER_ACTION,
                        ),
                    generate =
                        GenerateSettings(
                            historySave = HistorySavePreferences.NEVER_SAVE,
                        ),
                )

            val result = sut.getFullSettings().first()

            assertEquals(expected, result)
        }

    @Test
    fun `given datastore with haptic feedback false, toggle sets it to true`() =
        runTest(testScope.coroutineContext) {
            sut.getFullSettings().test {
                assertFalse(awaitItem().scan.hapticFeedback)

                sut.toggleScanHapticFeedback()

                assertTrue(awaitItem().scan.hapticFeedback)
            }
        }

    @Test
    fun `given some datastore, updating field returns new settings in flow`() =
        runTest(testScope.coroutineContext) {
            sut.getFullSettings().test {
                awaitItem()

                sut.setOpenUrlPreferences(OpenUrlPreferences.IN_CUSTOM_TAB)

                assertEquals(OpenUrlPreferences.IN_CUSTOM_TAB, awaitItem().general.openUrlPreferences)
            }
        }
}
