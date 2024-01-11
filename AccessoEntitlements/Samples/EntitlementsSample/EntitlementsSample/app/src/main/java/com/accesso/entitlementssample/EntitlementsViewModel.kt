package com.accesso.entitlementssample

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accesso.AccessoCore
import com.accesso.error.AccessoSDKNetworkException
import com.accessosdk.accessoentitlements.AccessoEntitlements
import com.accessosdk.accessoentitlements.models.Entitlement
import com.accessosdk.accessoentitlements.models.RegistrationResponse
import com.accessosdk.accessowallet.AccessoWallet
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class EntitlementsViewModel: ViewModel() {
    private val dialogMessage = MutableStateFlow(DialogMessage())
    internal lateinit var accessoEntitlements: AccessoEntitlements
    internal lateinit var accessoWallet: AccessoWallet
    internal lateinit var entitlement: Entitlement

    /*
    can be any string but need to keep track of it for updating.
    Also every pass needs to have a unique ID
    Make sure to update this if you are attempting to add a new pass.
    If the ID is already existing, will will just add that existing pass to your Googgle Wallet
    rather than the new one you are attempting to create.
     */
    val passId = "test1"

    private val entitlementsMutableLiveData: MutableLiveData<List<Entitlement>?> = MutableLiveData()
    val entitlementsLiveData: LiveData<List<Entitlement>?> = entitlementsMutableLiveData

    private val claimEntitlementsMutableLiveData: MutableLiveData<RegistrationResponse> = MutableLiveData()
    val claimEntitlementsLiveData: LiveData<RegistrationResponse> = claimEntitlementsMutableLiveData

    private val isValidInputMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isValidInputLiveData: LiveData<Boolean> = isValidInputMutableLiveData

    private val addedToWalletMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val addedToWalletLiveData: LiveData<Boolean> = addedToWalletMutableLiveData

    private val isWalletAvailableMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isWalletAvailableLiveData: LiveData<Boolean> = isWalletAvailableMutableLiveData

    private val showAddToWalletButtonMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val showAddToWalletButtonLiveData: LiveData<Boolean> = showAddToWalletButtonMutableLiveData

    private val updatePassResponseMutableLiveData: MutableLiveData<String> = MutableLiveData()
    val updatePassResponseLiveData: LiveData<String> = updatePassResponseMutableLiveData

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SecurityException -> {
                setDialog("Error", "Security Exception", "OK", null, {}, {})
            }
            is AccessoSDKNetworkException -> {
                setDialog("Error", "A network error occurred. Please try again.", "OK", null, {}, {})
            }
            else -> {
                setDialog(
                    "Error",
                    "An unexpected error occurred. Please try again.",
                    "OK",
                    null,
                    {},
                    {})
            }
        }
    }

    fun setDialog(
        title: String,
        message: String,
        confirmText: String,
        dismissText: String?,
        confirmedPressed: () -> Unit,
        dismissPressed: () -> Unit
    ) {
        dialogMessage.value = DialogMessage(
            true,
            title,
            message,
            confirmText,
            dismissText,
            confirmedPressed,
            dismissPressed
        )
    }
    fun initializeEntitlements(applicationContext: Context, activity: Activity) {

        viewModelScope.launch(exceptionHandler){
            try {
                AccessoCore
                    .setApplicationContext(applicationContext)
                    .setActivity(activity)
                    .build()

                accessoEntitlements = AccessoEntitlements()
                accessoWallet = AccessoWallet()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun addToWallet() {
        viewModelScope.launch {
            val passRequest = accessoEntitlements.createWalletPass(entitlement, passId)
            accessoWallet.addToWallet(passRequest)
        }
    }

    fun updatePass() {
        viewModelScope.launch {
            val passRequest = accessoEntitlements.createWalletPass(entitlement, passId)
            passRequest.ticketHolder = "new first new last"
            accessoWallet.updatePass(passRequest)
        }
    }

    fun shouldShowWalletButton() {
        showAddToWalletButtonMutableLiveData.postValue(true)
    }

    fun isWalletAvailable() {
        viewModelScope.launch {
            isWalletAvailableMutableLiveData.postValue(accessoWallet.isWalletAvailable())
        }
    }

    fun addToWalletSuccess() {
        addedToWalletMutableLiveData.postValue(true)
    }

    fun addToWalletFail() {
        addedToWalletMutableLiveData.postValue(false)
    }

    fun getEntitlements() {
        viewModelScope.launch(exceptionHandler) {
             entitlementsMutableLiveData.postValue(accessoEntitlements.getEntitlements())
        }
    }

    fun claimEntitlements(barcode: String) {
        viewModelScope.launch(exceptionHandler) {
            claimEntitlementsMutableLiveData.postValue(accessoEntitlements.claimEntitlement(barcode = barcode))
        }
    }

    fun claimEntitlements(orderId: String, orderEmail: String) {
        viewModelScope.launch(exceptionHandler) {
            claimEntitlementsMutableLiveData.postValue(accessoEntitlements.claimEntitlement(orderId = orderId, orderEmail = orderEmail))
        }
    }

    fun isValidInput(value: Boolean) {
        isValidInputMutableLiveData.postValue(value)
    }

    fun scanBarcode() {
        viewModelScope.launch(exceptionHandler) {
            val barcodeString = AccessoCore.scanBarcode()
            if (barcodeString != null) {
                claimEntitlements(barcodeString)
            }
        }
    }
}