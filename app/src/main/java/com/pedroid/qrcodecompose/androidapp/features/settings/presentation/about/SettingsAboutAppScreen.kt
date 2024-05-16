package com.pedroid.qrcodecompose.androidapp.features.settings.presentation.about

import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.viewinterop.AndroidView
import com.pedroid.qrcodecompose.androidapp.R
import com.pedroid.qrcodecompose.androidapp.designsystem.components.QRAppToolbar
import com.pedroid.qrcodecompose.androidapp.designsystem.theme.Dimens
import com.pedroid.qrcodecompose.androidapp.designsystem.utils.BaseQRCodeAppPreview
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.SettingsAboutAppActionListeners
import com.pedroid.qrcodecompose.androidapp.features.settings.navigation.about.SettingsAboutAppNavigationListeners

// region screen composables
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsAboutAppScreen(
    appVersion: String,
    appBuildNumber: String,
    externalNavigationListeners: SettingsAboutAppNavigationListeners,
    actionListeners: SettingsAboutAppActionListeners,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        QRAppToolbar(
            modifier = Modifier.fillMaxWidth(),
            titleRes = R.string.settings_main_screen_other_about_the_app_title,
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
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            start = Dimens.spacingExtraLarge,
                            end = Dimens.spacingExtraLarge,
                            top = Dimens.spacingMedium,
                        ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Using an Android view because using a Painter does not work
                //  this did not work either https://gist.github.com/tkuenneth/ddf598663f041dc79960cda503d14448
                AndroidView(
                    modifier = Modifier.size(Dimens.largeIconSize),
                    factory = { context ->
                        ImageView(context).apply {
                            setImageResource(R.mipmap.ic_launcher)
                        }
                    },
                )
                Spacer(modifier = Modifier.width(Dimens.spacingMedium))
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            Spacer(modifier = Modifier.width(Dimens.spacingExtraSmall))
            Text(
                text =
                    stringResource(
                        id = R.string.settings_about_the_app_version,
                        appVersion,
                        appBuildNumber,
                    ),
                color = MaterialTheme.colorScheme.secondary,
            )
            Spacer(modifier = Modifier.width(Dimens.spacingExtraLarge))
            Text(
                modifier = Modifier.padding(horizontal = Dimens.spacingExtraLarge, vertical = Dimens.spacingMedium),
                text = stringResource(id = R.string.settings_about_the_app_information),
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(top = Dimens.spacingMedium, bottom = Dimens.spacingLarge),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.End),
                    onClick = actionListeners.onOpenGithub,
                ) {
                    Text(text = stringResource(id = R.string.settings_about_the_app_open_source_link))
                }
                VerticalDivider(
                    modifier = Modifier.fillMaxHeight().padding(horizontal = Dimens.spacingMedium, vertical = Dimens.spacingExtraSmall),
                    thickness = Dimens.borderWidthSmall,
                )
                TextButton(
                    modifier = Modifier.weight(1f).wrapContentWidth(Alignment.Start),
                    onClick = actionListeners.onOpenPrivacyPolicy,
                ) {
                    Text(text = stringResource(id = R.string.settings_about_the_app_privacy_policy_link))
                }
            }
            Spacer(modifier = Modifier.height(Dimens.spacingExtraSmall))
            TextButton(onClick = actionListeners.onOpenMoreApps) {
                Text(text = stringResource(id = R.string.settings_about_the_app_other_apps_link))
            }
        }
    }
}

// endregion screen composables

// region screen previews
@PreviewScreenSizes
@Composable
fun SettingsAboutAppScreenPreview() {
    BaseQRCodeAppPreview(modifier = Modifier.fillMaxSize()) {
        SettingsAboutAppScreen(
            appVersion = "1.0.0",
            appBuildNumber = "951",
            externalNavigationListeners = SettingsAboutAppNavigationListeners(),
            actionListeners = SettingsAboutAppActionListeners(),
        )
    }
}
// endregion screen composables
