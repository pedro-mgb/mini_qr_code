package com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageData
import com.pedroid.qrcodecompose.androidapp.core.presentation.composables.TemporaryMessageType
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsAboutAppViewModel
    @Inject
    constructor(
        settingsRepository: SettingsRepository,
    ) : ViewModel() {
        private val temporaryMessageFlow = MutableStateFlow<TemporaryMessageData?>(null)
        private val uiFlow =
            combine(
                settingsRepository.getFullSettings(),
                temporaryMessageFlow,
            ) { settings, tmpMessage ->
                SettingsAboutAppUIState(settings.general.openUrlPreferences, tmpMessage)
            }
        val uiState: StateFlow<SettingsAboutAppUIState> =
            this.createStateFlow(
                originalFlow = uiFlow,
                initialValue = SettingsAboutAppUIState(),
            )

        fun onNewAction(action: SettingsAboutAppUIAction) {
            viewModelScope.launch {
                when (action) {
                    is SettingsAboutAppUIAction.OpenUrlAttempt -> {
                        if (action.action.status != ActionStatus.SUCCESS) {
                            temporaryMessageFlow.emit(
                                TemporaryMessageData(
                                    text = "error_unable_to_open_url",
                                    type = TemporaryMessageType.ERROR_SNACKBAR,
                                ),
                            )
                        }
                    }
                    is SettingsAboutAppUIAction.TmpMessageShown -> {
                        temporaryMessageFlow.emit(null)
                    }
                }
            }
        }
    }

data class SettingsAboutAppUIState(
    val openUrlMode: OpenUrlPreferences = OpenUrlPreferences.DEFAULT,
    val temporaryMessage: TemporaryMessageData? = null,
)

sealed class SettingsAboutAppUIAction {
    data class OpenUrlAttempt(val action: QRAppActions.OpenApp) : SettingsAboutAppUIAction()

    data object TmpMessageShown : SettingsAboutAppUIAction()
}
