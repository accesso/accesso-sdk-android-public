package com.accesso.queueingsample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accesso.ConfigService
import com.accesso.identity.api.IdentityRetrofitClientFactory
import com.accesso.identity.repository.IdentityRepository
import com.accesso.identity.services.IdentityService
import com.accesso.queueingsample.models.AuthState
import com.accesso.queueingsample.models.SignInStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _authState =
        MutableStateFlow<AuthState>(AuthState.Authenticated) // Default to Authenticated
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    val signInStatusLiveData: LiveData<SignInStatus> = MutableLiveData()

    private val identityRetrofitClient =
        IdentityRetrofitClientFactory.create(
            ConfigService.apiUrl,
            ConfigService.customerId,
            ConfigService.identityCredentials
        )
    private val identityRepository = IdentityRepository()
    private var identityService =
        IdentityService(
            identityRetrofitClient,
            identityRepository,
            ConfigService.applicationContext
        )

    init {
        viewModelScope.launch {
            identityService.isThereAnyToken(ConfigService.applicationContext)
                .collect { isThereAnyToken ->
                    if (isThereAnyToken) {
                        _authState.value = AuthState.Authenticated
                    } else {
                        _authState.value = AuthState.RequireAuthentication
                    }
                }
        }
    }

    fun signIn(account: String, pass: String) {
        viewModelScope.launch {
            val status = try {
                identityService.getCredentialTokenFromAPI(account, pass)
                SignInStatus.Success
            } catch (e: Exception) {
                SignInStatus.Failure("Error message")
            }
            (signInStatusLiveData as MutableLiveData<SignInStatus>).postValue(status)
        }
    }
}
