package com.liskovsoft.youtubeapi.service.internal

import android.content.Context
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase

private val PREF_NAME = MediaServiceCache::class.java.simpleName
private const val MEDIA_SERVICE_CACHE = "media_service_cache"

internal class MediaServiceCache(context: Context) : SharedPreferencesBase(context, PREF_NAME) {
    fun setMediaServiceCache(cache: String) {
        putString(MEDIA_SERVICE_CACHE, cache)
    }

    fun getMediaServiceCache(): String? = getString(MEDIA_SERVICE_CACHE, null)
}