package com.accesso.sdk_android_experience_promoter_sample.model

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
data class DialogMessage(
    var shouldShowDialog: Boolean = false,
    var title: String = "",
    var message: String = "",
    var confirmText: String = "",
    var dismissText: String? = null,
    var onConfirmButtonPressed: () -> Unit = {},
    var onDismissedButtonPressed: () -> Unit = {}
)