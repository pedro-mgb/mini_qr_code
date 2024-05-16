package com.pedroid.qrcodecompose.androidapp.features.settings.data

import androidx.datastore.core.DataStoreFactory
import com.pedroid.qrcodecompose.androidapp.features.settings.data.proto.SettingsProtoSerializer
import kotlinx.coroutines.CoroutineScope
import org.junit.rules.TemporaryFolder

fun TemporaryFolder.testUserPreferencesDataStore(
    coroutineScope: CoroutineScope,
    userPreferencesSerializer: SettingsProtoSerializer = SettingsProtoSerializer(),
    fileName: String = "user_preferences_test.pb",
) = DataStoreFactory.create(
    serializer = userPreferencesSerializer,
    scope = coroutineScope,
) {
    newFile(fileName)
}
