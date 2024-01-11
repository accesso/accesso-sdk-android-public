package com.accesso.sdk_android_experience_promoter_sample.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.accessosdk.experiencepromoter.model.Message
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.util.*
import kotlin.collections.HashSet

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 */
object SharedPrefsUtils {
    val PREFERENCES = SharedPrefsUtils::class.java.getPackage()!!.name + ".preferences"
    private const val KEY_READ_MESSAGE_IDS = "read-message-ids"
    const val KEY_EXPIRED_MESSAGE_IDS = "expired-message-ids"
    private const val KEY_CACHED_FEATURED_MESSAGE = "cached_featured_message"
    private const val TAG = "SharedPrefsUtils"
    val APP_USER_ID = "app_user_id"
    private val gson = Gson()

    fun isMessageRead(
        sharedPreferences: SharedPreferences,
        message: Message
    ): Boolean {
        val messageIds: Set<String> = getReadMessageIds(sharedPreferences)
        return messageIds.contains(message.msgId)
    }

    fun getAppUserId(sharedPreferences: SharedPreferences): String {
        var userId = sharedPreferences.getString(APP_USER_ID, "")
        if (userId.isNullOrEmpty()) {
            sharedPreferences.edit().putString(APP_USER_ID, UUID.randomUUID().toString()).apply()
            userId = sharedPreferences.getString(APP_USER_ID, "")
        }
        return userId ?: ""
    }

    @JvmStatic
    fun getReadMessageIds(sharedPreferences: SharedPreferences): MutableSet<String> {
        return sharedPreferences.getStringSet(KEY_READ_MESSAGE_IDS, HashSet<String>()) ?: HashSet()
    }

    fun getExpiredMessageIds(sharedPreferences: SharedPreferences): MutableSet<String> {
        return sharedPreferences.getStringSet(KEY_EXPIRED_MESSAGE_IDS, HashSet()) ?: HashSet()
    }

    @JvmStatic
    fun markMessageAsRead(
        sharedPreferences: SharedPreferences,
        messageId: String
    ): Boolean? {
        val editor = sharedPreferences.edit()
        val messageIds = getReadMessageIds(sharedPreferences)
        val wasAdded = messageId.let { messageIds.add(it) }
        editor.putStringSet(KEY_READ_MESSAGE_IDS, messageIds)
        editor.apply()
        return wasAdded
    }

    fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).apply { }
    }

    fun markMessageAsExpired(
        sharedPreferences: SharedPreferences,
        message: Message
    ): Boolean? {
        val editor = sharedPreferences.edit()
        val messageIds = getExpiredMessageIds(sharedPreferences)
        val wasAdded = message.msgId.let { messageIds.add(it) }
        editor.putStringSet(KEY_EXPIRED_MESSAGE_IDS, messageIds)
        editor.apply()
        return wasAdded
    }

    @JvmStatic
    fun clearMarkedMessages(context: Context) {
        val prefs = getPreferences(context)
        prefs.edit().clear().apply()
    }

    fun getCachedFeaturedMessage(context: Context): Message? {
        val prefs = getPreferences(context)
        var featuredMessageJson: String? = null
        return try {
            featuredMessageJson = prefs.getString(KEY_CACHED_FEATURED_MESSAGE, null)
            gson.fromJson(featuredMessageJson, Message::class.java)
        } catch (e: JsonSyntaxException) {
            Log.d(TAG, "featuredMessageJson: $featuredMessageJson")
            Log.e(TAG, "getCachedFeaturedMessage: ", e)
            null
        } catch (e: NumberFormatException) {
            Log.d(TAG, "featuredMessageJson: $featuredMessageJson")
            Log.e(TAG, "getCachedFeaturedMessage: ", e)
            null
        }
    }

    fun writeFeaturedMessageToCache(context: Context, message: Message?) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        val messageJson = gson.toJson(message)
        editor.putString(KEY_CACHED_FEATURED_MESSAGE, messageJson)
        editor.apply()
    }
}