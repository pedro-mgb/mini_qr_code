package com.pedroid.qrcodecompose.androidapp.core.presentation

import com.pedroid.qrcodecompose.androidapp.core.domain.ActionStatus
import com.pedroid.qrcodecompose.androidapp.core.domain.QRAppActions
import org.junit.Assert.assertEquals
import org.junit.Test

internal class QRAppActionsTest {
    @Test
    fun `GIVEN success in open app action, THEN there is no TemporaryMessage`() {
        val action = QRAppActions.OpenApp(ActionStatus.SUCCESS)

        val result = action.asTemporaryMessage()

        assertEquals(null, result)
    }

    @Test
    fun `GIVEN success in share to app action, THEN there is no TemporaryMessage`() {
        val action = QRAppActions.ShareApp(ActionStatus.SUCCESS)

        val result = action.asTemporaryMessage()

        assertEquals(null, result)
    }

    @Test
    fun `GIVEN success in copy action, THEN returns SUCCESS_SNACKBAR TemporaryMessage`() {
        val action = QRAppActions.Copy(ActionStatus.SUCCESS)

        val result = action.asTemporaryMessage()

        assertEquals(TemporaryMessageType.SUCCESS_SNACKBAR, result?.type)
    }

    @Test
    fun `GIVEN success in save to file action, THEN returns SUCCESS_SNACKBAR TemporaryMessage`() {
        val action = QRAppActions.SaveToFile(ActionStatus.SUCCESS)

        val result = action.asTemporaryMessage()

        assertEquals(TemporaryMessageType.SUCCESS_SNACKBAR, result?.type)
    }

    @Test
    fun `GIVEN no app error in open app action, THEN returns ERROR_SNACKBAR TemporaryMessage`() {
        val action = QRAppActions.OpenApp(ActionStatus.ERROR_NO_APP)

        val result = action.asTemporaryMessage()

        assertEquals(TemporaryMessageType.ERROR_SNACKBAR, result?.type)
    }

    @Test
    fun `GIVEN no app error in share to app action, THEN returns ERROR_SNACKBAR TemporaryMessage`() {
        val action = QRAppActions.ShareApp(ActionStatus.ERROR_NO_APP)

        val result = action.asTemporaryMessage()

        assertEquals(TemporaryMessageType.ERROR_SNACKBAR, result?.type)
    }

    @Test
    fun `GIVEN no app error in copy action, THEN there is no TemporaryMessage`() {
        val action = QRAppActions.Copy(ActionStatus.ERROR_NO_APP)

        val result = action.asTemporaryMessage()

        assertEquals(null, result)
    }

    @Test
    fun `GIVEN no app error in save to file action, THEN there is no TemporaryMessage`() {
        val action = QRAppActions.SaveToFile(ActionStatus.ERROR_NO_APP)

        val result = action.asTemporaryMessage()

        assertEquals(null, result)
    }

    @Test
    fun `GIVEN file error for any app action, THEN returns ERROR_SNACKBAR TemporaryMessage`() {
        val actions: List<QRAppActions> =
            ActionStatus.ERROR_FILE.let { status ->
                listOf(
                    QRAppActions.OpenApp(status),
                    QRAppActions.ShareApp(status),
                    QRAppActions.Copy(status),
                    QRAppActions.SaveToFile(status),
                )
            }

        actions.forEach { action ->
            val result = action.asTemporaryMessage()

            assertEquals(TemporaryMessageType.ERROR_SNACKBAR, result?.type)
        }
    }
}
