package com.accesso.sdk_android_experience_promoter_sample.view

import android.Manifest
import android.app.ActivityManager
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.accesso.sdk_android_experience_promoter_sample.SdkExperiencePromoterSampleApp
import com.accesso.sdk_android_experience_promoter_sample.navigation.*
import com.accesso.sdk_android_experience_promoter_sample.notifications.NotificationDTO
import com.accesso.sdk_android_experience_promoter_sample.notifications.NotificationListener
import com.accesso.sdk_android_experience_promoter_sample.util.SharedPrefsUtils
import com.accesso.sdk_android_experience_promoter_sample.view.theme.ExperiencePromoterTheme
import com.accesso.sdk_android_experience_promoter_sample.viewmodel.MessagesViewModel
import com.accessosdk.experiencepromoter.model.MessageEvent

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>MainActivity</b> - The Starting point of the <b>Sample App</b>
 * Generates basic auth credentials, gets or generates the appUserId, initializes the <b>Accesso Messages SDK</b>
 * and listens for push notifications
 *
 */
class MainActivity : ComponentActivity() {

    internal lateinit var viewModel: MessagesViewModel
    internal lateinit var navController: NavHostController
    private lateinit var sharedPreferences: SharedPreferences

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.setPushPermissionsGranted(this)
        }
    }

    private fun askNotificationPermission() {
        if (isOnTiramisu()) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                viewModel.setDialog(
                    "Enable Push Notifications",
                    "Would you like to enable push notifications?",
                    "OK",
                    "No thanks",
                    confirmedPressed = { requestPushPermission() },
                    dismissPressed = {}
                )
            } else {
                requestPushPermission()
            }
        }
    }

    fun isOnTiramisu(): Boolean {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPushPermission() {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = SharedPrefsUtils.getPreferences(this)

        viewModel =
            MessagesViewModel(sharedPreferences)
        viewModel.setPushPermissionsGranted(this)
        viewModel.initializeMessageModule(
            applicationContext = applicationContext,
            activity = this,
            listenForBeacons = false
        )


        setContent {
            ExperiencePromoterTheme()
        }

        SdkExperiencePromoterSampleApp.getInstance().notificationListener =
            object : NotificationListener {
                override fun onReceivedNotification(notificationDTO: NotificationDTO) {
                    viewModel.setSnackbarToShowMessage(notificationDTO.data.alert)
                    viewModel.refreshMessages()
                    val appInForegroundMessageActionEvent = if (isAppInForeground()) {
                        MessageEvent.MessageAction.PushWhileOpen
                    } else {
                        MessageEvent.MessageAction.PushWhileNotOpen
                    }
                    viewModel.trackMessageAction(
                        notificationDTO.messageIdString,
                        MessageEvent.MessageAction.Received
                    )
                    viewModel.trackMessageAction(
                        notificationDTO.messageIdString,
                        appInForegroundMessageActionEvent
                    )
                }
            }

    }

    @Composable
    fun ExperiencePromoterTheme() {
        val isPushEnabled by viewModel.isPushEnabledState.collectAsState()
        val snackbarVisible by viewModel.showSnackbarState.collectAsState()
        val snackbarMessage by viewModel.snackbarMessageState.collectAsState()

        ExperiencePromoterTheme {
            navController = rememberNavController()

            // Show a dialog if called for
            Dialog(viewModel)

            Column(modifier = Modifier.background(Color(0xFF96A8F5))) {
                NavHostGraph(navController = navController, viewModel)
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                if (isOnTiramisu() && !isPushEnabled)
                    Button(
                        onClick = {
                            askNotificationPermission()
                        }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 36.dp, vertical = 12.dp)
                            .testTag("home_enable_notifications_button"),
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.DarkGray,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Enable Notifications")
                    }

                if (snackbarVisible) {
                    Snackbar(
                        action = {
                            TextButton(
                                onClick = { viewModel.clearSnackbarMessage()}
                            ) {
                                Text(text = "DISMISS", modifier = Modifier.testTag("message_snackbar_dismiss_button"))
                            }
                        },
                        modifier = Modifier.testTag("message_snackbar")
                    ) {
                        Text(text = snackbarMessage, modifier = Modifier.testTag("message_snackbar_text"))
                    }
                }
            }
        }
    }

    fun isAppInForeground(): Boolean {
        val appProcessInfo = ActivityManager.RunningAppProcessInfo()
        ActivityManager.getMyMemoryState(appProcessInfo)
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
    }

    companion object {
        const val REQUEST_PERMISSIONS_REQUEST_CODE = 12
    }
}
