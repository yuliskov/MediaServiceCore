package com.liskovsoft.youtubeapi.app.nsigsolver.common

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase
import com.liskovsoft.youtubeapi.app.AppService

private const val PREF_NAME = "yt_cache_service"
private const val KEY_DELIM = "%KEY%"
private const val FIELD_DELIM = "%FIELD%"
private const val UNKNOWN = "UNKNOWN"

internal open class CacheError(message: String, cause: Exception? = null): Exception(message, cause)

internal data class CachedData(
    val code: String,
    val version: String = UNKNOWN,
    val variant: String = UNKNOWN
) {
    override fun toString(): String {
        return Helpers.merge(FIELD_DELIM, code, version, variant)
    }

    companion object {
        fun fromString(data: String): CachedData {
            val split = Helpers.split(data, FIELD_DELIM)

            val code = Helpers.parseStr(split, 0)
                ?: throw CacheError("Cannot restore CachedData item. At least 'code' field should be set")
            val version = Helpers.parseStr(split, 1) ?: UNKNOWN
            val variant = Helpers.parseStr(split, 2) ?: UNKNOWN

            return CachedData(code, version, variant)
        }
    }
}

internal object CacheService: SharedPreferencesBase(AppService.instance().context, PREF_NAME) {
    private val storage: MutableMap<String, MutableMap<String, CachedData?>> = mutableMapOf()

    fun load(section: String, key: String): CachedData? {
        if (!storage.contains(section))
            storage[section] = mutableMapOf()

        if (storage[section]?.let { !it.contains(key) } ?: false)
            storage[section]?.put(key, loadFromStorage(section, key))

        return storage[section]?.get(key)
    }

    fun store(section: String, key: String, content: CachedData) {
        val sectionStorage = storage[section] ?: mutableMapOf<String, CachedData?>().also { storage[section] = it }
        sectionStorage[key] = content

        persistToStorage(section, key, content)
    }

    private fun loadFromStorage(section: String, key: String): CachedData? {
        val data = getString("$section$KEY_DELIM$key", null)

        return data?.let { CachedData.fromString(it) }
    }

    private fun persistToStorage(section: String, key: String, content: CachedData) {
        putString("$section$KEY_DELIM$key", content.toString())
    }
}
