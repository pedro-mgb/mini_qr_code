package com.pedroid.qrcodecompose.androidapp.features.settings.presentation.contact

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppToolbar
import com.pedroid.qrcodecompose.androidapp.designsystem.icons.outlined.Browser
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.SettingsContactActionListeners
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.contact.SettingsContactNavigationListeners

// region screen composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContactScreen(
    externalNavigationListeners: SettingsContactNavigationListeners,
    actionListeners: SettingsContactActionListeners,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QRAppToolbar(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.settings_main_screen_other_contact_developer_title,
            onNavigationIconClick = externalNavigationListeners.onGoBack,
        )
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .weight(0.99f)
                    .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.spacingExtraLarge, vertical = Dimens.spacingMedium),
                text = stringResource(id = R.string.settings_contact_developer_description),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingLarge))
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(top = Dimens.spacingMedium, bottom = Dimens.spacingLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ContactDeveloperButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                icon = Icons.Outlined.Browser,
                text = stringResource(id = R.string.settings_contact_developer_github_issue_action),
                onClick = actionListeners.onContactViaGithubIssue,
            )
            Spacer(modifier = Modifier.height(Dimens.spacingSmall))
            ContactDeveloperButton(
                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        contentColor = MaterialTheme.colorScheme.onTertiary,
                    ),
                icon = Icons.Outlined.Email,
                text = stringResource(id = R.string.settings_contact_developer_email_action),
                onClick = actionListeners.onContactViaEmail,
            )
        }
    }
}

@Composable
private fun ContactDeveloperButton(
    modifier: Modifier = Modifier,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        colors = colors,
        onClick = onClick,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null)
            Spacer(modifier = Modifier.width(Dimens.spacingMedium))
            Text(text = text)
        }
    }
}
// endregion screen composables

// region screen previews
@PreviewScreenSizes
@Composable
fun SettingsContactScreenPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        SettingsContactScreen(
            externalNavigationListeners = SettingsContactNavigationListeners(),
            actionListeners = SettingsContactActionListeners(),
        )
    }
}
// endregion screen composables
