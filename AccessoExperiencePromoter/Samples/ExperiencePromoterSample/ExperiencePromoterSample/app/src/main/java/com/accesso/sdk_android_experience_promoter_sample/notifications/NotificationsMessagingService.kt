package com.accesso.sdk_android_experience_promoter_sample.notifications


import android.util.Log
import com.accesso.sdk_android_experience_promoter_sample.SdkExperiencePromoterSampleApp
import com.accessosdk.experiencepromoter.AccessoExperiencePromoter
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>NotificationsMessagingService</b> - The class used for handling Firebase notifications
 *
 * @see FirebaseMessagingService
 */
class NotificationsMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        try {
            AccessoExperiencePromoter.updatePushDeviceToken(token)
        } catch (exception: Exception) {
            Log.w(
                TAG,
                "Exception was thrown when calling updatePushDeviceToken: ${exception.message}"
            )
            exception.printStackTrace()
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notificationData = NotificationsUtils.parseRemoteMessage(remoteMessage)
        SdkExperiencePromoterSampleApp.getInstance().onReceivedNotification(notificationData)
        NotificationsUtils.showPushNotification(notificationData)
    }
    companion object{
        const val TAG = "NotificationsMessagingService"
    }

}