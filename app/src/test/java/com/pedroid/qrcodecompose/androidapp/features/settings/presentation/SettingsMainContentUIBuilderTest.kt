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
                GeneralSettings("", OpenUrlPreferences.IN_CUSTOM_TAB),
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
        assertEquals(result.items.size, 2)
        assertTrue(result.items[0] is SettingsMainContentItem.OptionWithActionSelection)
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).text, "scan_save_history")
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).currentOption, "upon_user_action")
        assertTrue(result.items[1] is SettingsMainContentItem.OptionWithToggle)
        assertContains((result.items[1] as SettingsMainContentItem.OptionWithToggle).text, "haptic_feedback")
        assertEquals((result.items[1] as SettingsMainContentItem.OptionWithToggle).toggleOn, true)
    }

    @Test
    fun `generateSettingsItems in correct order`() {
        val result = sut.generateSettingsItems(GenerateSettings(HistorySavePreferences.NEVER_SAVE))

        assertContains(result.headerText, "scan")
        assertEquals(result.items.size, 2)
        assertTrue(result.items[0] is SettingsMainContentItem.OptionWithActionSelection)
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).text, "scan_save_history")
        assertContains((result.items[0] as SettingsMainContentItem.OptionWithActionSelection).currentOption, "upon_user_action")
    }

    @Test
    fun `otherSettingsItems in correct order`() {
        // TODO implement
    }
}
