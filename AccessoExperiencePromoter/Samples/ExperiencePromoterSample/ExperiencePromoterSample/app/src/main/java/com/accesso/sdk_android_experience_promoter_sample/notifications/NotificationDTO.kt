package com.accesso.sdk_android_experience_promoter_sample.notifications

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * <b>NotificationDTO</b> - Object containing data held within a notification
 *
 * @param messageIdString The message ID
 * @param messageType The type of the message as in Int
 * @param data The data as a [DataJson]
 *
 * @see DataJson
 *
 */
data class NotificationDTO(
    val messageIdString: String,
    val messageType: Int,
    val data: DataJson
)

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
@Parcelize
data class DataJson(
    @SerializedName("alert") val alert: String,
    @SerializedName("content-available") val contentAvailable: Int,
    @SerializedName("mutable-content") val mutableContent: Int
) : Parcelable
