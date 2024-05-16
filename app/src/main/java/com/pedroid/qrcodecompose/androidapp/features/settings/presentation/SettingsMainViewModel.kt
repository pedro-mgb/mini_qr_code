package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pedroid.qrcodecompose.androidapp.core.logging.Logger
import com.pedroid.qrcodecompose.androidapp.core.presentation.createStateFlow
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.FullSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOG_TAG = "SettingsMainViewModel"

@HiltViewModel
class SettingsMainViewModel
    @Inject
    constructor(
        private val settingsRepository: SettingsRepository,
        private val settingsMainContentUIBuilder: SettingsMainContentUIBuilder,
        private val languageManager: LanguageManager,
        private val logger: Logger,
    ) : ViewModel() {
        private val originalFlow: Flow<Pair<FullSettings, AppLanguage>>
            get() =
                combine(
                    settingsRepository.getFullSettings(),
                    languageManager.getAppLanguageFlow(),
                ) { fullSettings, language ->
                    Pair(fullSettings, language)
                }

        val uiState: StateFlow<SettingsMainUIState> =
            this.createStateFlow(
                originalFlow = originalFlow,
                initialValue = SettingsMainUIState.Idle,
                mapper = ::mapToUIState,
            )

        private fun mapToUIState(settingsData: Pair<FullSettings, AppLanguage>): SettingsMainUIState {
            logger.debug(LOG_TAG, "new settings item $settingsData")
            val settings = settingsData.first
            return SettingsMainUIState.Content(
                sections =
                    listOf(
                        settingsMainContentUIBuilder.generalSettingsItems(
                            settings.general,
                            currentLanguage = settingsData.second,
                        ),
                        settingsMainContentUIBuilder.scanSettingsItems(settings.scan),
                        settingsMainContentUIBuilder.generateSettingsItems(settings.generate),
                        settingsMainContentUIBuilder.otherSettingsItems(),
                    ),
            )
        }

        fun onNewAction(action: SettingsMainUIAction) {
            viewModelScope.launch {
                logger.debug(LOG_TAG, "onNewAction - $action")
                when (action) {
                    is SettingsMainUIAction.ChangeAppLanguage -> {
                        languageManager.setAppLanguage(action.newLanguage)
                    }
                    is SettingsMainUIAction.ChangeOpenUrlMode -> {
                        settingsRepository.setOpenUrlPreferences(action.newMode)
                    }
                    is SettingsMainUIAction.ChangeScanHistorySave -> {
                        settingsRepository.setScanHistorySavePreferences(action.option)
                    }
                    SettingsMainUIAction.ToggleScanHapticFeedback -> {
                        settingsRepository.toggleScanHapticFeedback()
                    }
                    is SettingsMainUIAction.ChangeGenerateHistorySave -> {
                        settingsRepository.setGenerateHistorySavePreferences(action.option)
                    }
                }
            }
        }
    }

sealed class SettingsMainUIState {
    data object Idle : SettingsMainUIState()

    data class Content(val sections: List<SettingsMainContentSection>) : SettingsMainUIState()

    companion object {
        fun emptyContent() = Content(sections = emptyList())
    }
}

sealed class SettingsMainUIAction {
    data class ChangeAppLanguage(val newLanguage: AppLanguage) : SettingsMainUIAction()

    data class ChangeOpenUrlMode(val newMode: OpenUrlPreferences) : SettingsMainUIAction()

    data class ChangeScanHistorySave(val option: HistorySavePreferences) : SettingsMainUIAction()

    data object ToggleScanHapticFeedback : SettingsMainUIAction()

    data class ChangeGenerateHistorySave(val option: HistorySavePreferences) : SettingsMainUIAction()
}
