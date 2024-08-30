package com.accesso.sdk_android_experience_promoter_sample

import android.app.Application
import android.content.Context
import com.accesso.sdk_android_experience_promoter_sample.notifications.NotificationDTO
import com.accesso.sdk_android_experience_promoter_sample.notifications.NotificationListener

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>AdkMessageSampleApp</b> - Handles the notifications
 *
 */
class SdkExperiencePromoterSampleApp : Application() {

    var notificationListener: NotificationListener? = null

    override fun onCreate() {
        instance = this
        super.onCreate()
    }

    fun onReceivedNotification(notificationDTO: NotificationDTO) {
        notificationListener?.onReceivedNotification(notificationDTO)
    }

    companion object {
        private lateinit var instance: SdkExperiencePromoterSampleApp

        fun getInstance(): SdkExperiencePromoterSampleApp {
            return instance
        }

        fun getContext(): Context {
            return instance
        }
    }
}