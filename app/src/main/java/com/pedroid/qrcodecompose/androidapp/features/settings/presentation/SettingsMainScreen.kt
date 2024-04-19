package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.SettingsMainActionListeners

// region screen composables
@Composable
fun SettingsMainScreen(actionListeners: SettingsMainActionListeners) {
    // TODO implement feature
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.to_be_implemented),
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingExtraLarge))
        Text(
            modifier = Modifier.padding(horizontal = Dimens.spacingMedium),
            text = stringResource(id = R.string.settings_contact_developer_description),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Dimens.spacingExtraSmall))
        TextButton(onClick = actionListeners.onContactDeveloperByEmail) {
            Text(
                modifier = Modifier.padding(horizontal = Dimens.spacingMedium),
                text = stringResource(id = R.string.settings_contact_developer_email_action),
                textAlign = TextAlign.Center,
            )
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewFontScale
@Composable
fun SettingsMainScreenPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        SettingsMainScreen(SettingsMainActionListeners())
    }
}
// endregion screen previews
