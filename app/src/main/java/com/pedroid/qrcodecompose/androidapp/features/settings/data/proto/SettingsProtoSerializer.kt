package com.pedroid.qrcodecompose.androidapp.features.settings.data.proto

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HAPTIC_FEEDBACK_DEFAULT_OFF
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.LANGUAGE_DEFAULT_SAME_AS_SYSTEM
import java.io.InputStream
import java.io.OutputStream

object SettingsProtoSerializer: Serializer<SettingsProto> {
    override val defaultValue: SettingsProto =
        SettingsProto.getDefaultInstance()
            .toBuilder()
            .setAppLanguage(LANGUAGE_DEFAULT_SAME_AS_SYSTEM)
            .setScanHapticFeedback(HAPTIC_FEEDBACK_DEFAULT_OFF)
            .build()

    override suspend fun readFrom(input: InputStream): SettingsProto =
        try {
            SettingsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    override suspend fun writeTo(t: SettingsProto, output: OutputStream) = t.writeTo(output)
}
