package com.accesso.queueingsample.models

sealed class SignInStatus {
    object Success : SignInStatus()
    data class Failure(val message: String) : SignInStatus()
}
