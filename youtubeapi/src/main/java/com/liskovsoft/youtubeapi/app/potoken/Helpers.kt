package com.liskovsoft.youtubeapi.app.potoken

import android.util.Base64

internal open class BGError(
    val code: String,
    message: String,
    val info: Map<String, Any>? = null
) : Exception(message)

private val base64UrlCharRegex = Regex("[-_]")

private val base64UrlToBase64Map = mapOf(
    '-' to '+',
    '_' to '/'
)

internal fun base64ToU8(base64: String): ByteArray {
    var base64Mod = base64

    if (base64UrlCharRegex.containsMatchIn(base64)) {
        base64Mod = buildString(base64.length) {
            for (c in base64) {
                append(base64UrlToBase64Map[c] ?: c)
            }
        }
    }

    // Add padding if missing (JS atob tolerates this)
    val padding = (4 - base64Mod.length % 4) % 4
    if (padding != 0) {
        base64Mod += "=".repeat(padding)
    }

    return Base64.decode(base64Mod, Base64.DEFAULT)
}

internal fun u8ToBase64(u8: ByteArray, base64url: Boolean = false): String {
    val flags = if (base64url) {
        Base64.NO_PADDING or Base64.NO_WRAP or Base64.URL_SAFE
    } else {
        Base64.NO_WRAP
    }

    return Base64.encodeToString(u8, flags)
}
