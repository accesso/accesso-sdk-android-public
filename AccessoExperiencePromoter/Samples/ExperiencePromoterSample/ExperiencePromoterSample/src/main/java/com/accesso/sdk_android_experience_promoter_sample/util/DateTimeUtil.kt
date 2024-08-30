package com.accesso.sdk_android_experience_promoter_sample.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

/**
 * @suppress - TODO https://accesso.atlassian.net/browse/MSDK-2136
 * Takes a dateTime String of the input format parses it to the output format and returns the result
 * as a String
 * @param dateTimeStr the string to format
 * @param inputFormat the format of the string to be reformatted (ex: "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
 * @param outputFormat the format you wish to have returned (ex: "MMMM dd, yyyy")
 * @return the [dateTimeStr] formatted from [inputFormat] to [outputFormat]
 * exomple:
 * ```
 *      val formattedDate = formatDateTime(
 *                              "2023-04-27T20:11:44.447+0000",
 *                              "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
 *                              "MMMM dd, yyyy")
 *      //formattedDate would be "April 27, 2023"
 * ```
 */
@SuppressLint("SimpleDateFormat")
fun formatDateTime(dateTimeStr: String, inputFormat:String, outputFormat: String): String {
    val simpleDateFormat = SimpleDateFormat(inputFormat)
    simpleDateFormat.timeZone = TimeZone.getDefault()
    val publishedTime = simpleDateFormat.parse(dateTimeStr)
    return publishedTime?.getUIStringDate(outputFormat) ?: ""
}

private fun Date.getUIStringDate(format: String): String {
    val dateFormat = SimpleDateFormat(format,
        Locale.getDefault())
    dateFormat.timeZone = TimeZone.getDefault()
    return dateFormat.format(this)
}
