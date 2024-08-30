package com.accesso.sdk_android_experience_promoter_sample.model

import okhttp3.OkHttpClient

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
data class DeviceInfo(
    val apiUrl: String,
    val credentails: String,
    val appUserId: String,
    val okHttpClient: OkHttpClient,
    val venueId: String
    )
