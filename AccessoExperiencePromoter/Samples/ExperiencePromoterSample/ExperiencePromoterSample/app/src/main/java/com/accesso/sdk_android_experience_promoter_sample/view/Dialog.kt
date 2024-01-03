package com.accesso.sdk_android_experience_promoter_sample.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Composable
fun Dialog(
    viewModel: MessagesViewModel,
) {
    val dialogMessage by viewModel.dialogMessageState.collectAsState()

    if (dialogMessage.shouldShowDialog) {
        AlertDialog(
            modifier = Modifier.testTag("alert_dialog"),
            onDismissRequest = { dialogMessage.onConfirmButtonPressed },
            title = { Text(text = dialogMessage.title) },
            text = { Text(text = dialogMessage.message) },
            confirmButton = {
                TextButton(
                    modifier = Modifier.testTag("alert_confirm_button"),
                    onClick = {
                        dialogMessage.onConfirmButtonPressed()
                        viewModel.closeDialog()
                    }
                ) {
                    Text(text = dialogMessage.confirmText)
                }
            },
            dismissButton = if (dialogMessage.dismissText != null) {
                {
                    TextButton(
                        modifier = Modifier.testTag("alert_dismiss_button"),
                        onClick = {
                        dialogMessage.onDismissedButtonPressed()
                        viewModel.closeDialog()
                    }) {
                        Text(text = dialogMessage.dismissText!!)
                    }
                }
            } else null
        )
    }
}