package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.core.presentation.PreviewAppLocales
import com.pedroid.qrcodecompose.androidapp.core.presentation.stringResourceFromStringKey
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.SettingsMainActionListeners

// region screen composables
@Composable
fun SettingsMainScreen(
    content: SettingsMainUIState.Content,
    actionListeners: SettingsMainActionListeners,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
                .verticalScroll(rememberScrollState())
                .padding(Dimens.spacingMedium),
    ) {
        Text(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(Dimens.spacingSmall),
            text = stringResource(id = R.string.settings_main_screen_title),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.size(Dimens.spacingMedium))
        MainSettingsSections(
            sections = content.sections,
            actionListeners,
        )
    }
}

@Composable
private fun MainSettingsSections(
    sections: List<SettingsMainContentSection>,
    actionListeners: SettingsMainActionListeners,
) {
    sections.forEach { section ->
        if (section.items.isNotEmpty()) {
            MainSettingsSectionHeader(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = Dimens.spacingSmall),
                text = section.headerText,
                icon = section.headerIcon,
            )
            section.items.forEach { item ->
                MainSettingsItem(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                item.invokeAction(actionListeners)
                            }
                            .padding(start = Dimens.spacingSmall, bottom = Dimens.spacingMedium),
                    sectionItem = item,
                    actionListeners = actionListeners,
                )
            }
            Spacer(modifier = Modifier.size(Dimens.spacingSmall))
        }
    }
}

@Composable
private fun MainSettingsSectionHeader(
    modifier: Modifier = Modifier,
    text: String,
    icon: ImageVector,
) {
    Column(modifier = modifier) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = Dimens.spacingExtraSmall),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(Dimens.mediumIconSize),
                imageVector = icon,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = null,
            )
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spacingMedium),
                text = stringResourceFromStringKey(text),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun MainSettingsItem(
    modifier: Modifier = Modifier,
    sectionItem: SettingsMainContentItem,
    actionListeners: SettingsMainActionListeners,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = sectionItem.startIcon, contentDescription = null)
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = Dimens.spacingSmall),
        ) {
            Text(text = stringResourceFromStringKey(stringKey = sectionItem.text))
            Text(
                text = stringResourceFromStringKey(stringKey = sectionItem.context),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            )
        }
        when (sectionItem) {
            is SettingsMainContentItem.OptionWithActionSelection -> {
                Box(modifier = Modifier.size(Dimens.mediumIconSize)) {
                    // no additional content to place, just to create an empty space
                }
            }
            is SettingsMainContentItem.OptionWithToggle -> {
                Switch(
                    checked = sectionItem.toggleOn,
                    onCheckedChange = {
                        actionListeners.onUIAction(sectionItem.toggleAction)
                    },
                )
            }
            is SettingsMainContentItem.OptionWithExternalScreenAction -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    tint = MaterialTheme.colorScheme.outline,
                    contentDescription = null,
                )
            }
        }
    }
}

private fun SettingsMainContentItem.invokeAction(listeners: SettingsMainActionListeners) {
    when (this) {
        is SettingsMainContentItem.OptionWithActionSelection -> {
            listeners.onShowSettingsOptionSelection(this)
        }
        is SettingsMainContentItem.OptionWithToggle -> {
            listeners.onUIAction(this.toggleAction)
        }
        is SettingsMainContentItem.OptionWithExternalScreenAction -> {
            listeners.onExternalNavigationAction(this.action)
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewFontScale
@PreviewAppLocales
@Composable
fun SettingsMainScreenPreview() {
    val sectionsForPreview =
        remember {
            SettingsMainContentUIBuilder().let { builder ->
                listOf(
                    builder.generalSettingsItems(GeneralSettings(), AppLanguage.SAME_AS_SYSTEM),
                    builder.scanSettingsItems(ScanSettings()),
                    builder.generateSettingsItems(GenerateSettings()),
                    builder.otherSettingsItems(),
                )
            }
        }
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        SettingsMainScreen(
            content = SettingsMainUIState.Content(sectionsForPreview),
            actionListeners = SettingsMainActionListeners(),
        )
    }
}
// endregion screen previews
