package com.accesso.queueingsample

data class DialogMessage(
    var shouldShowDialog: Boolean = false,
    var title: String = "",
    var message: String = "",
    var confirmText: String = "",
    var dismissText: String? = null,
    var onConfirmButtonPressed: () -> Unit = {},
    var onDismissedButtonPressed: () -> Unit = {}
)