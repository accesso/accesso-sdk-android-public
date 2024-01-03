package com.accesso.sdk_android_experience_promoter_sample.notifications

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>NotificationListener</b> - Interface used for receiving notifications.
 *
 * @see NotificationDTO
 *
 */
fun interface NotificationListener {

    fun onReceivedNotification(notificationDTO: NotificationDTO)

}
