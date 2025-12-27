package com.liskovsoft.youtubeapi.innertube.utils

import com.google.gson.GsonBuilder
import com.liskovsoft.sharedutils.helpers.Helpers
import java.util.regex.Pattern

/**
 * Finds a string between two delimiters.
 * @param data the data
 * @param startString start string
 * @param endString end string
 * @return the string between start and end, or null if not found
 */
internal fun getStringBetweenStrings(data: String, startString: String, endString: String): String? {
    val regex = escapeStringRegexp(startString) + "(.*?)" + escapeStringRegexp(endString)
    val pattern = Pattern.compile(regex, Pattern.DOTALL)
    val matcher = pattern.matcher(data)
    if (matcher.find()) {
        return matcher.group(1)
    } else {
        return null
    }
}

/**
 * Escapes a string to be used in a regex.
 * @param input input string
 * @return escaped string
 */
internal fun escapeStringRegexp(input: String): String {
    // Escape special regex characters
    val escaped = input.replace("([|\\\\{}()\\[\\]^$+*?.])".toRegex(), "\\\\$1")
    // Replace dash
    return escaped.replace("-", "\\x2d")
}

internal enum class DeviceCategory {
    MOBILE,
    DESKTOP
}

internal fun UserAgents.byCategory(category: DeviceCategory): List<String> =
    when (category) {
        DeviceCategory.DESKTOP -> desktop
        DeviceCategory.MOBILE -> mobile
    }

internal fun getRandomUserAgent(type: DeviceCategory): String {
    return UserAgents
        .byCategory(type)
        .random()
}

internal fun toJsonString(obj: Any): String {
    val gson = GsonBuilder().create() // nulls are ignored by default
    return gson.toJson(obj)
}


/**
 * Generates a random string with the given length.
 *
 */
internal fun generateRandomString(length: Int): String {
    val alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_"
    val result = StringBuilder(length)

    repeat(length) {
        result.append(alphabet[Helpers.getRandom().nextInt(alphabet.length)])
    }

    return result.toString()
}