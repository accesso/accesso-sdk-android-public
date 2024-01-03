package com.accesso.queueingsample

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accesso.AccessoCore
import com.accesso.error.AccessoSDKNetworkException
import com.accessosdk.accessoqueueing.AccessoQueueing
import com.accessosdk.accessoqueueing.model.Attraction
import com.accessosdk.accessoqueueing.model.ReservationResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AttractionViewModel : ViewModel() {
    internal lateinit var accessoQueueing: AccessoQueueing

    private val attractionsMutableLiveData: MutableLiveData<List<Attraction>?> = MutableLiveData()
    val attractionsLiveData: LiveData<List<Attraction>?> = attractionsMutableLiveData

    private var selectedAttractionMutableLiveData: MutableLiveData<Attraction?> =
        MutableLiveData()
    val selectedAttractionLiveData: LiveData<Attraction?> = selectedAttractionMutableLiveData

    private var attractionByIdMutableLiveData: MutableLiveData<Attraction?> =
        MutableLiveData()
    val attractionByIdLiveData: LiveData<Attraction?> = attractionByIdMutableLiveData

    private val mutableDialogMessageState = MutableStateFlow(DialogMessage())
    val dialogMessageState: StateFlow<DialogMessage> = mutableDialogMessageState

    private val reservationResponseMutableLiveData = MutableLiveData<ReservationResponse>()
    val reservationResponseLiveData: LiveData<ReservationResponse> = reservationResponseMutableLiveData

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
        mutableDialogMessageState.value = DialogMessage(
            true,
            title,
            message,
            confirmText,
            dismissText,
            confirmedPressed,
            dismissPressed
        )
    }

    fun closeDialog() {
        mutableDialogMessageState.value = dialogMessageState.value.copy(shouldShowDialog = false)
    }

    fun setSelectedAttraction(attraction: Attraction) {
        selectedAttractionMutableLiveData.postValue(attraction)

        viewModelScope.launch(exceptionHandler) {
            try {
                val attractionById = accessoQueueing.getAttractionById(attraction.id)
                attractionByIdMutableLiveData.postValue(attractionById)
            } catch (exception: java.lang.Exception) {
                exception.printStackTrace()
                throw exception
            }
        }
    }

    fun initializeQueueing(applicationContext: Context, activity: Activity) {
        viewModelScope.launch(exceptionHandler){
            try {
                AccessoCore
                    .setApplicationContext(applicationContext)
                    .setActivity(activity)
                    .build()
                accessoQueueing = AccessoQueueing()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getAttractions() {
        viewModelScope.launch(exceptionHandler) {
            attractionsMutableLiveData.postValue(accessoQueueing.getAttractions())
        }
    }

    fun reserve(reserveActionId: String, input: Map<String, Any>? ) {
        viewModelScope.launch(exceptionHandler) {
            val response = accessoQueueing.reserve(reserveActionId, input)
            reservationResponseMutableLiveData.postValue(response)
        }
    }
}
