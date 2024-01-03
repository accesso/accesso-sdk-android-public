package com.accesso.sdk_android_experience_promoter_sample.model

import android.os.Parcelable
import androidx.annotation.StringDef
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * @author sebastian.dombrowski@mobileforming.com
 * @author daniel.vournazos@mobileforming.com
 * 11/31/16.
 */
@Parcelize
@JsonClass(generateAdapter = true)
class MessageAction(
    @get:MessageActionTypes
    @MessageActionTypes
    val action: String? = null,
    val label: String? = null,
    val target: String? = null,
    val linkId : Int = 0,
    val appNav: String? = null) : Parcelable {

    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    @StringDef(
            TYPE_ACTION_POI_DETAIL,
            TYPE_ACTION_OFFER,
            TYPE_ACTION_URL,
            TYPE_ACTION_PRODUCT,
            TYPE_ACTION_EVENT)
    annotation class MessageActionTypes

    companion object {
        const val TYPE_ACTION_POI_DETAIL = "poiDetail"
        const val TYPE_ACTION_OFFER = "offer"
        const val TYPE_ACTION_URL = "url"
        const val TYPE_ACTION_PRODUCT = "product"
        const val TYPE_ACTION_EVENT = "event"
        const val TYPE_ACTION_APP_SCREEN = "appScreen"
    }
}