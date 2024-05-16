package com.pedroid.qrcodecompose.androidapp.features.settings.presentation

import com.pedroid.qrcodecompose.androidapp.core.test.assertContains
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GeneralSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.GenerateSettings
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.HistorySavePreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.OpenUrlPreferences
import com.pedroid.qrcodecompose.androidapp.features.settings.domain.ScanSettings
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingsMainContentUIBuilderTest {
    private val sut = SettingsMainContentUIBuilder()

    @Test
    fun `generalSettingsItems in correct order`() {
        val result =
            sut.generalSettingsItems(
                GeneralSettings(OpenUrlPreferences.IN_CUSTOM_TAB),
                AppLanguage.PORTUGUESE,
            )

        assertContains(result.headerText, "general")
        assertEquals(2, result.items.size)
        assertTrue(result.items[0] is SettingsMainContentItem.OptionWithActionSelection)
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).text, "language")
        assertEquals(
            AppLanguage.PORTUGUESE.translationKey,
            (result.items[0] as SettingsMainContentItem.OptionWithActionSelection).currentOption,
        )
        assertEquals(
            SettingsMainUIAction.ChangeAppLanguage(AppLanguage.ENGLISH),
            (result.items[0] as SettingsMainContentItem.OptionWithActionSelection).selectionContent.onSelect(1),
        )
        assertTrue(result.items[1] is SettingsMainContentItem.OptionWithActionSelection)
        assertContains((result.items[1] as SettingsMainContentItem.OptionWithActionSelection).text, "url")
        assertContains((result.items[1] as SettingsMainContentItem.OptionWithActionSelection).currentOption, "custom_tab")
        assertEquals(
            SettingsMainUIAction.ChangeOpenUrlMode(OpenUrlPreferences.IN_BROWSER),
            (result.items[1] as SettingsMainContentItem.OptionWithActionSelection).selectionContent.onSelect(0),
        )
    }

    @Test
    fun `scanSettingsItems in correct order`() {
        val result = sut.scanSettingsItems(ScanSettings(true, HistorySavePreferences.UPON_USER_ACTION))

        assertContains(result.headerText, "scan")
        assertEquals(2, result.items.size)
        assertTrue(result.items[0] is SettingsMainContentItem.OptionWithActionSelection)
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).text, "scan_save_history")
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).currentOption, "upon_user_action")
        assertEquals(
            SettingsMainUIAction.ChangeScanHistorySave(HistorySavePreferences.NEVER_SAVE),
            (result.items[0] as SettingsMainContentItem.OptionWithActionSelection).selectionContent.onSelect(1),
        )
        assertTrue(result.items[1] is SettingsMainContentItem.OptionWithToggle)
        assertContains((result.items[1] as SettingsMainContentItem.OptionWithToggle).text, "haptic_feedback")
        assertEquals(true, (result.items[1] as SettingsMainContentItem.OptionWithToggle).toggleOn)
    }

    @Test
    fun `generateSettingsItems in correct order`() {
        val result = sut.generateSettingsItems(GenerateSettings(HistorySavePreferences.NEVER_SAVE))

        assertContains(result.headerText, "generate")
        assertEquals(1, result.items.size)
        assertTrue(result.items[0] is SettingsMainContentItem.OptionWithActionSelection)
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).text, "generate_save_history")
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).currentOption, "never")
        assertEquals(
            SettingsMainUIAction.ChangeGenerateHistorySave(HistorySavePreferences.UPON_USER_ACTION),
            (result.items[0] as SettingsMainContentItem.OptionWithActionSelection).selectionContent.onSelect(0),
        )
    }

    @Test
    fun `otherSettingsItems in correct order`() {
        val result = sut.otherSettingsItems()

        assertContains(result.headerText, "other")
        assertEquals(result.items.size, 3)
        assertTrue(result.items.all { it is SettingsMainContentItem.OptionWithExternalScreenAction })
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithExternalScreenAction).text, "contact")
        assertEquals(
            (result.items[0] as SettingsMainContentItem.OptionWithExternalScreenAction).action,
            SettingsExternalAction.ContactDeveloper,
        )
        assertContains((result.items[1] as SettingsMainContentItem.OptionWithExternalScreenAction).text, "rate")
        assertEquals((result.items[1] as SettingsMainContentItem.OptionWithExternalScreenAction).action, SettingsExternalAction.RateApp)
        assertContains((result.items[2] as SettingsMainContentItem.OptionWithExternalScreenAction).text, "about")
        assertEquals(
            (result.items[2] as SettingsMainContentItem.OptionWithExternalScreenAction).action,
            SettingsExternalAction.MoreInfoAboutApp,
        )
    }
}
