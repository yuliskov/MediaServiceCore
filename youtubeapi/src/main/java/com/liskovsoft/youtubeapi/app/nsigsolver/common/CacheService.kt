package com.liskovsoft.youtubeapi.app.nsigsolver.common

import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase
import com.liskovsoft.youtubeapi.app.AppService

internal data class CachedData(
    val code: String,
    val version: String? = null,
    val variant: String? = null
)

internal object CacheService {
    private const val PREF_NAME = "yt_cache_service"
    private const val KEY_DELIM = "%KEY%"

    fun load(section: String, key: String): CachedData? {
        return loadFromStorage(section, key)
    }

    fun store(section: String, key: String, content: CachedData) {
        persistToStorage(section, key, content)
    }

    private fun loadFromStorage(section: String, key: String): CachedData? {
        // Use standalone prefs per section to preserve RAM
        val prefs = SharedPreferencesBase(AppService.instance().context, getPrefsName(section))

        val code: String? = prefs.getString(getCodeKey(key), null)
        val version: String? = prefs.getString(getVersionKey(key), null)
        val variant: String? = prefs.getString(getVariantKey(key), null)

        return code?.let { CachedData(it, version, variant) }
    }

    private fun persistToStorage(section: String, key: String, content: CachedData) {
        // Use standalone prefs per section to preserve RAM
        val prefs = SharedPreferencesBase(AppService.instance().context, getPrefsName(section))

        prefs.clear() // free some RAM (one value per file)
        prefs.putString(getCodeKey(key), content.code)
        prefs.putString(getVersionKey(key), content.version)
        prefs.putString(getVariantKey(key), content.variant)
    }

    private fun getCodeKey(key: String) = "$key${KEY_DELIM}code"
    private fun getVersionKey(key: String) = "$key${KEY_DELIM}version"
    private fun getVariantKey(key: String) = "$key${KEY_DELIM}variant"
    private fun getPrefsName(section: String) = "$PREF_NAME$KEY_DELIM$section"
}
