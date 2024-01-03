package com.accesso.sdk_android_experience_promoter_sample.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accesso.AccessoCore
import com.accesso.error.AccessoSDKNetworkException
import com.accesso.networking.model.BeaconRegion
import com.accesso.sdk_android_experience_promoter_sample.BuildConfig
import com.accesso.sdk_android_experience_promoter_sample.model.DialogMessage
import com.accesso.sdk_android_experience_promoter_sample.util.Event
import com.accesso.sdk_android_experience_promoter_sample.util.SharedPrefsUtils
import com.accesso.sdk_android_experience_promoter_sample.util.isInternetAvailable
import com.accesso.sdk_android_experience_promoter_sample.view.MainActivity
import com.accessosdk.experiencepromoter.AccessoExperiencePromoter
import com.accessosdk.experiencepromoter.model.AppNotificationStateEvent
import com.accessosdk.experiencepromoter.model.Message
import com.accessosdk.experiencepromoter.model.MessageEvent
import com.accessosdk.experiencepromoter.model.PointOfInterest
import com.accessosdk.experiencepromoter.model.RegionEvent
import com.accessosdk.experiencepromoter.util.BeaconStatus
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>MessagesViewModel</b> - Instantiates and receives messages from the <b>[Accesso SDK]</b>.
 * Handles the logic of the sample app
 */
class MessagesViewModel(
    newSharedPreferences: SharedPreferences
) :
    ViewModel() {
    private var messagesMutableLiveData: MutableLiveData<SnapshotStateList<Message?>> =
        MutableLiveData()
    val messagesLiveData: LiveData<SnapshotStateList<Message?>> = messagesMutableLiveData
    private var selectedMessageMutableLiveData: MutableLiveData<Message?> =
        MutableLiveData()
    val selectedMessageLiveData: LiveData<Message?> = selectedMessageMutableLiveData
    private var beaconsMutableLiveData: MutableLiveData<List<BeaconRegion>> =
        MutableLiveData()
    val beaconsLiveData: LiveData<List<BeaconRegion>> = beaconsMutableLiveData
    private var messageByIdMutableLiveData: MutableLiveData<Message?> =
        MutableLiveData()
    val messageByIdLiveData: LiveData<Message?> = messageByIdMutableLiveData
    private var isListeningForBeaconsMutableLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isListeningForBeaconsLiveData: LiveData<Boolean> = isListeningForBeaconsMutableLiveData
    private val beaconEnterExitMap = mutableMapOf<BeaconRegion, Boolean>()
    private var beaconEnterExitMutableLiveData: MutableLiveData<Event<Map<BeaconRegion, Boolean>>> =
        MutableLiveData()
    val beaconEnterExitLiveData: LiveData<Event<Map<BeaconRegion, Boolean>>> =
        beaconEnterExitMutableLiveData

    private var pointsOfInterestMutableLiveData: MutableLiveData<SnapshotStateList<PointOfInterest?>> =
        MutableLiveData()
    val pointsOfInterestLiveData: LiveData<SnapshotStateList<PointOfInterest?>> =
        pointsOfInterestMutableLiveData
    private var selectedPointOfInterestLiveData: MutableLiveData<PointOfInterest> =
        MutableLiveData()
    val selectedPointOfInterest: LiveData<PointOfInterest?> = selectedPointOfInterestLiveData


    private val mutableDialogMessageState = MutableStateFlow(DialogMessage())
    val dialogMessageState: StateFlow<DialogMessage> = mutableDialogMessageState

    private val mutableIsPushEnabledState = MutableStateFlow(false)
    val isPushEnabledState: StateFlow<Boolean> = mutableIsPushEnabledState

    private val mutableShowSnackbarState = MutableStateFlow(false)
    val showSnackbarState: StateFlow<Boolean> = mutableShowSnackbarState

    private val mutableSnackbarMessageState = MutableStateFlow("")
    val snackbarMessageState: StateFlow<String> = mutableSnackbarMessageState

    var isModuleInitialized = false


    val sharedPreferences = newSharedPreferences
    private lateinit var accessoExperiencePromoter: AccessoExperiencePromoter

    private lateinit var context: Context

    internal val beaconListener: (BeaconRegion, Int) -> Unit = { beaconRegion, state ->
        if (state == BeaconStatus.ENTER.ordinal) {
            beaconEnterExitMap[beaconRegion] = true
            Log.d(
                "SampleAppBeacons",
                "Entered beacon range for beaconRegion=${beaconRegion}"
            )
        } else {
            beaconEnterExitMap[beaconRegion] = false
            Log.d(
                "SampleAppBeacons",
                "Exited beacon range for beaconRegion=${beaconRegion}"
            )
        }
        beaconEnterExitMutableLiveData.postValue(Event(beaconEnterExitMap))
    }

    companion object {
        const val TAG = "MessagesViewModel"
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        when (exception) {
            is SecurityException -> {
                setDialog("Error", "If you set during build .setPromptForLocationPermission(false), remember to implement Location permissions in the sample app.", "OK", null, {}, {})
            }
            is AccessoSDKNetworkException-> {
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

    fun initializeMessageModule(
        applicationContext: Context,
        activity: Activity,
        listenForBeacons: Boolean
    ) {
        context = applicationContext
        viewModelScope.launch(exceptionHandler) {
            isListeningForBeaconsMutableLiveData.postValue(listenForBeacons)

            Log.d(TAG, "appUserId=${SharedPrefsUtils.getAppUserId(sharedPreferences)}")

            if (isInternetAvailable(applicationContext)) {

                try {
                    AccessoCore
                        .setAppUserId(SharedPrefsUtils.getAppUserId(sharedPreferences))
                        .setApplicationContext(applicationContext)
                        .setOnBeaconRegionDetection(beaconListener)
                        .setActivity(activity)
                        .build()

                    accessoExperiencePromoter = AccessoExperiencePromoter()

                    registerToken()

                    if (isInternetAvailable(activity)) {
                        trackAppNotificationState(
                            AppNotificationStateEvent.getStateString(
                                NotificationManagerCompat.from(activity).areNotificationsEnabled()
                            )
                        )
                    }

                    if (hasLocationPermissions(activity)) {
                        accessoExperiencePromoter.startUpdatingLocation(activity = activity)
                    }

                    refreshMessages()
                    isModuleInitialized = true
                } catch (exception: java.lang.Exception) {
                    Log.w(TAG, "Exception was thrown when calling configure: ${exception.message}")
                    exception.printStackTrace()
                    throw exception
                }
            }
        }
    }

    private fun hasLocationPermissions(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * <b>refreshMessages</b> - fetches messages from the <b>[AccessoExperiencePromoter SDK]</b>
     * emits value as a [List] of [Message] to live data
     * @see Message
     *
     */
    fun refreshMessages() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val messages = accessoExperiencePromoter.getMessages()
                Log.d("messages", "Got these messages: $messages")
                messagesMutableLiveData.postValue(messages.body()?.toMutableStateList())
            } catch (exception: java.lang.Exception) {
                Log.w(TAG, "Exception was thrown when calling getAllMessages: ${exception.message}")
                exception.printStackTrace()
                throw exception
            }
        }
    }

    fun promptForLocationPermission(){
        viewModelScope.launch(exceptionHandler) {
            try {
                accessoExperiencePromoter.promptForLocationPermission()
            } catch (exception: java.lang.Exception) {
                Log.w(TAG, "Exception was thrown when calling promptForLocationPermission: ${exception.message}")
                exception.printStackTrace()
                throw exception
            }
        }
    }

    fun promptForBluetoothPermission(){
        viewModelScope.launch(exceptionHandler) {
            try {
                accessoExperiencePromoter.promptForBluetoothPermission()
            } catch (exception: java.lang.Exception) {
                Log.w(TAG, "Exception was thrown when calling promptForBluetoothPermission: ${exception.message}")
                exception.printStackTrace()
                throw exception
            }
        }
    }

    fun trackMessageAction(messageId: String, action: MessageEvent.MessageAction) {
        Log.d("messages", "Calling tracker event for this id: $messageId")

        // TODO: https://accesso.atlassian.net/browse/MSDK-817 weird that this gives a 200 back with basically no data
        val messageActionEvent = MessageEvent(
            appId = BuildConfig.APPLICATION_ID,
            appUserId = SharedPrefsUtils.getAppUserId(sharedPreferences),
            version = BuildConfig.VERSION_NAME,
            messageId = messageId,
            action = action,
            eventType = "message"
        )

        // TODO: https://accesso.atlassian.net/browse/MSDK-807 iOS calls markRead where we call this directly
        viewModelScope.launch(exceptionHandler) {
            try {
                accessoExperiencePromoter.trackMessageAction(messageActionEvent)

                if (action == MessageEvent.MessageAction.Read) {
                    SharedPrefsUtils.markMessageAsRead(sharedPreferences, messageId)
                }

            } catch (exception: java.lang.Exception) {
                Log.w(
                    TAG,
                    "Exception was thrown when calling trackMessageAction: ${exception.message}"
                )
                exception.printStackTrace()
                throw exception
            }
        }

    }

    fun trackAppNotificationState(state: String) {
        val appNotificationStateEvent = AppNotificationStateEvent(
            appId = BuildConfig.APPLICATION_ID,
            appUserId = SharedPrefsUtils.getAppUserId(sharedPreferences),
            version = BuildConfig.VERSION_NAME,
            state = state
        )

        viewModelScope.launch {
            accessoExperiencePromoter.trackAppNotificationState(
                appNotificationStateEvent
            )
        }
    }

    private fun registerToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            val token = task.result

            try {
                AccessoExperiencePromoter.updatePushDeviceToken(token)
            } catch (exception: Exception) {
                Log.w(
                    TAG,
                    "Exception was thrown when calling updatePushDeviceToken: ${exception.message}"
                )
                exception.printStackTrace()
            }
        })
    }

    fun fetchPointsOfInterest() {
        viewModelScope.launch(exceptionHandler) {
            try {
                val poiList = accessoExperiencePromoter.getPointsOfInterest()
                pointsOfInterestMutableLiveData.postValue(poiList.toMutableStateList())
            } catch (exception: java.lang.Exception) {
                Log.w(
                    TAG,
                    "Exception was thrown when calling fetchPointsOfInterestResponse: ${exception.message}"
                )
                exception.printStackTrace()
                throw exception
            }
        }
    }

    fun setSnackbarToShowMessage(message: String) {
        mutableShowSnackbarState.value = true
        mutableSnackbarMessageState.value = message
    }

    fun clearSnackbarMessage() {
        mutableShowSnackbarState.value = false
        mutableSnackbarMessageState.value = ""
    }

    fun setPushPermissionsGranted(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            mutableIsPushEnabledState.value = (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    fun setSelectedMessage(message: Message) {
        selectedMessageMutableLiveData.postValue(message)

        viewModelScope.launch(exceptionHandler) {
            try {
                val messageResponse =
                    accessoExperiencePromoter.getMessageById(message.msgId)?.body()
                messageByIdMutableLiveData.postValue(messageResponse)
            } catch (exception: java.lang.Exception) {
                Log.w(
                    TAG,
                    "Exception was thrown when calling getMessageByIdResponse: ${exception.message}"
                )
                exception.printStackTrace()
                throw exception
            }
        }
    }

    fun setSelectedPOI(pointOfInterest: PointOfInterest) {
        selectedPointOfInterestLiveData.postValue(pointOfInterest)
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

    fun trackLocationUpdates(location: Location) {
        viewModelScope.launch(exceptionHandler) {
            try {
                accessoExperiencePromoter.trackDeviceLocation(
                    RegionEvent.LocationInfo(
                        location.latitude,
                        location.longitude,
                        location.accuracy,
                    )
                )
            } catch (exception: java.lang.Exception) {
                Log.w(TAG, "Exception was thrown when calling getAllMessages: ${exception.message}")
                exception.printStackTrace()
            }
        }
    }

    fun fetchBeacons() {
        viewModelScope.launch {
            try {
                val beacons = accessoExperiencePromoter.getBeaconRegions().body()
                beaconsMutableLiveData.postValue(beacons)
            } catch (ex: java.lang.Exception) {
                Log.w(TAG, "Exception was thrown when calling fetchBeacons: ${ex.message}")
                ex.printStackTrace()
            }
        }
    }

    fun setListeningForBeacons(value: Boolean) {
        isListeningForBeaconsMutableLiveData.value = value
    }

    fun startListeningForBeacons(beaconRegions: List<BeaconRegion>) {
        if (isListeningForBeaconsLiveData.value == false) {
            isListeningForBeaconsMutableLiveData.postValue(true)
            viewModelScope.launch {
                accessoExperiencePromoter.startListeningForBeaconRegions(
                    beaconRegions,
                    beaconListener
                )
            }
        }
    }

    fun stopListeningForBeacons(beaconRegions: List<BeaconRegion>) {
        if (isListeningForBeaconsLiveData.value == true) {
            isListeningForBeaconsMutableLiveData.postValue(false)
            viewModelScope.launch {
                accessoExperiencePromoter.stopListeningForBeaconRegions(beaconRegions)
            }
        }

    }
    fun onLocationUpdates(activity: Activity) {
        val locationManager = activity.getSystemService(ComponentActivity.LOCATION_SERVICE) as LocationManager

        if (ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.REQUEST_PERMISSIONS_REQUEST_CODE
            )
            return
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            3600000,
            5f
        ) { location ->
            // When the location changes, store the current location in the parent activity
            Log.d("Location", location.toString())
            trackLocationUpdates(location)
        }
    }

    fun showEvents() {
        accessoExperiencePromoter.showEvents(context, 10)
    }

    internal fun setAccessoExperiencePromoter(experiencePromoter: AccessoExperiencePromoter) {
        // set the message view model's AEP to the mocked instance
        accessoExperiencePromoter = experiencePromoter
    }
}
