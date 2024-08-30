package com.accesso.sdk_android_experience_promoter_sample.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.accesso.sdk_android_experience_promoter_sample.R
import com.accesso.sdk_android_experience_promoter_sample.SdkExperiencePromoterSampleApp
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.random.Random

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>NotificationsUtils</b> - utils for Firebase notifications
 *
 * @see FirebaseMessagingService
 */
object NotificationsUtils {
    private const val CHANNEL_ID: String = "CHANNEL_ID"

    /**
     * <b>parseRemoteMessage</b> - Parses the notification
     * @param remoteMessage the [RemoteMessage]
     *
     * @return [NotificationDTO]
     */
    fun parseRemoteMessage(remoteMessage: RemoteMessage): NotificationDTO {
        val data = Bundle()
        val remoteData = remoteMessage.data
        if (remoteData.isNotEmpty()) {
            for ((key, value) in remoteData) {
                data.putString(key, value)
            }
        }
        return data.toNotificationDto()
    }

    /**
     * <b>showPushNotification</b> - Shows the push notification
     * @param notificationData the [NotificationDTO]
     *
     * @return [NotificationDTO]
     */
    fun showPushNotification(notificationData: NotificationDTO) {
        createNotificationChannel()
        val builder =
            NotificationCompat.Builder(SdkExperiencePromoterSampleApp.getInstance(), CHANNEL_ID)
                .setContentTitle(notificationData.data.alert)
                .setSmallIcon(R.drawable.ic_baseline_notifications_none_24)
                .setContentText(notificationData.data.alert)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(SdkExperiencePromoterSampleApp.getContext())) {
            if (ActivityCompat.checkSelfPermission(
                    SdkExperiencePromoterSampleApp.getContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(Random.nextInt(), builder.build())
        }
    }

    /**
     * <b>createNotificationChannel</b> - Creates the notification channel
     * @see NotificationManager
     * @see NotificationChannel
     * @see NotificationManagerCompat
     */
    private fun createNotificationChannel() {
        val name = SdkExperiencePromoterSampleApp.getInstance().getString(R.string.channel_name)
        val descriptionText =
            SdkExperiencePromoterSampleApp.getInstance().getString(R.string.channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        NotificationManagerCompat.from(SdkExperiencePromoterSampleApp.getContext())
            .createNotificationChannel(channel)
    }
}

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
fun Bundle.toNotificationDto() = NotificationDTO(
    messageIdString = getString("i").orEmpty(),
    messageType = getString("t")?.toInt() ?: 0,
    data = Gson().fromJson(getString("aps")!!)
)

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
inline fun <reified T> Gson.fromJson(json: String): T =
    fromJson(json, object : TypeToken<T>() {}.type)