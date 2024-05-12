package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.lifecycle.ViewModel
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsMainViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val settingsMainContentUIBuilder: SettingsMainContentUIBuilder,
        private val languageManager: LanguageManager,
    ) : ViewModel() {
        val uiState: StateFlow<SettingsMainUIState> =
            this.createStateFlow(
                originalFlow = settingsRepository.getFullSettings(),
                initialValue = SettingsMainUIState.Idle,
                mapper = ::mapToUIState,
            )

        private fun mapToUIState(settings: FullSettings): SettingsMainUIState =
            SettingsMainUIState.Content(
                sections =
                    listOf(
                        settingsMainContentUIBuilder.generalSettingsItems(settings.general, languageManager.getAppLanguage()),
                        settingsMainContentUIBuilder.scanSettingsItems(settings.scan),
                        settingsMainContentUIBuilder.generateSettingsItems(settings.generate),
                        settingsMainContentUIBuilder.otherSettingsItems(),
                    ),
            )

        fun onNewAction(action: SettingsMainUIAction) {
        }
    }

sealed class SettingsMainUIState {
    data object Idle : SettingsMainUIState()

    data class Content(val sections: List<SettingsMainContentSection>) : SettingsMainUIState()
}

sealed class SettingsMainUIAction {
    data class ChangeAppLanguage(val newLanguage: AppLanguage) : SettingsMainUIAction()

    data class ChangeOpenUrlMode(val newMode: OpenUrlPreferences) : SettingsMainUIAction()

    data class ChangeScanHistorySave(val option: HistorySavePreferences) : SettingsMainUIAction()

    data object ToggleScanHapticFeedback : SettingsMainUIAction()

    data class ChangeGenerateHistorySave(val option: HistorySavePreferences) : SettingsMainUIAction()
}
