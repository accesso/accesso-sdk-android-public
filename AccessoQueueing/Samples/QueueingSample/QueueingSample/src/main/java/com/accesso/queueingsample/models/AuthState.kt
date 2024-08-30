package com.accesso.queueingsample.models

sealed class AuthState {
    object Authenticated : AuthState()
    object RequireAuthentication : AuthState()

}
