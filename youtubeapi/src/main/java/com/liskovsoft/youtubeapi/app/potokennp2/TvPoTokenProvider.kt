package com.liskovsoft.youtubeapi.app.potokennp2

import android.content.Context
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenGenerator
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenResult
import com.liskovsoft.youtubeapi.app.potokennp2.generators.TvPoTokenWebView4
import com.liskovsoft.youtubeapi.app.potokennp2.misc.runOnMainThreadSync
import com.liskovsoft.youtubeapi.common.helpers.AppClient

/**
 * The living room (TV) client has its own BotGuard request key and challenge (tv_config).
 * Its poToken is a session one: it is bound to the living room token id (the /tv page)
 * and not to the video id.
 */
internal object TvPoTokenProvider {
    private val TAG = TvPoTokenProvider::class.simpleName
    private val lock = Any()
    private const val FORCE_ATT_CHALLENGE = true
    private var generator: PoTokenGenerator? = null
    private var result: PoTokenResult? = null
    @Volatile
    private var expirationMs: Long = -1
    // MOD: the session token is saved to disk and reused after the app restart (until it expires)
    private const val PREFS_NAME = "tv_potoken_cache"
    private const val MIN_REMAINING_MS = 10 * 60_000L
    @Volatile
    private var isRestored = false
    private var isRestoreTried = false

    /**
     * The id that should be passed to the player request: tvAppInfo.livingRoomPoTokenId
     */
    var livingRoomPoTokenId: String? = null
        private set

    fun isPotExpired(): Boolean = System.currentTimeMillis() > expirationMs

    fun getPoToken(): PoTokenResult? {
        if (!WebPoTokenProvider.isPotSupported()) {
            return null
        }

        return try {
            synchronized(lock) { getPoTokenSync() }
        } catch (e: Exception) {
            Log.e(TAG, "Could not obtain TV poToken", e)
            reset()
            null
        }
    }

    /** Clears all state, including the generator and cached tokens. The next call regenerates everything. */
    fun reset() {
        synchronized(lock) {
            if (isRestored || result != null) clearSaved()
            isRestored = false
            result = null
            livingRoomPoTokenId = null
            expirationMs = -1
            release()
        }
    }

    /** Frees the generator to reduce memory. Cached values are kept; the generator is recreated on demand. */
    fun release() {
        synchronized(lock) {
            val old = generator
            generator = null
            old?.let { runOnMainThreadSync(3) { it.close() } }
        }
    }

    /** Returns true when the current token came from disk. Then the token is removed and the next call generates a new one. */
    fun discardRestored(): Boolean {
        synchronized(lock) {
            if (!isRestored) return false
            Log.w(TAG, "Discarding the restored TV poToken")
            reset()
            return true
        }
    }

    private fun getPoTokenSync(): PoTokenResult? {
        result?.let { if ((generator != null || isRestored) && !isPotExpired()) return it }

        restoreSaved()?.let { return it }

        val tvConfig = AppService.instance().tvConfig
        val requestKey = tvConfig?.challengeRequestKey
        // May be absent (e.g. with the Cobalt user agent). Then the program is taken from /att/get.
        val challenge = if (FORCE_ATT_CHALLENGE) null else tvConfig?.challenge

        if (requestKey == null) {
            Log.e(TAG, "tv_config doesn't contain the BotGuard request key")
            return null
        }

        if (challenge == null) {
            Log.w(TAG, "tv_config doesn't contain the BotGuard challenge. Using /att/get")
        }

        // The id and the visitor data should come from the same /tv page
        val appInfo = AppService.instance().getAppInfo(DefaultHeaders.APP_USER_AGENT)
        val livingRoomId = appInfo?.livingRoomPoTokenId
        val visitorData = appInfo?.visitorData

        if (livingRoomId == null || visitorData == null) {
            Log.e(TAG, "The /tv page doesn't contain the living room token id or the visitor data")
            return null
        }

        reset()

        val newGenerator = TvPoTokenWebView4.newPoTokenGenerator(AppService.instance().context, requestKey, challenge, AppClient.TV)
        val sessionPot = newGenerator.generatePoToken(livingRoomId)

        // Log.d(TAG, "TV poToken: livingRoomId=$livingRoomId, pot=$sessionPot, visitor_data=$visitorData")

        generator = newGenerator
        expirationMs = newGenerator.expirationMs
        livingRoomPoTokenId = livingRoomId
        // Same session token is used both for the player request and the streaming
        result = PoTokenResult("", visitorData, sessionPot, sessionPot)
        isRestored = false

        save(visitorData, sessionPot, livingRoomId, expirationMs)

        return result
    }

    private fun prefs() = AppService.instance().context?.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** Any error here only disables the cache. The normal generation runs after it. */
    private fun restoreSaved(): PoTokenResult? {
        if (isRestoreTried) return null
        isRestoreTried = true

        return try {
            val p = prefs() ?: return null
            val savedExpirationMs = p.getLong("expirationMs", -1)
            val visitorData = p.getString("visitorData", null)
            val pot = p.getString("pot", null)
            val livingRoomId = p.getString("livingRoomId", null)

            if (visitorData.isNullOrEmpty() || pot.isNullOrEmpty() || livingRoomId.isNullOrEmpty() ||
                savedExpirationMs - System.currentTimeMillis() < MIN_REMAINING_MS) {
                clearSaved()
                return null
            }

            expirationMs = savedExpirationMs
            livingRoomPoTokenId = livingRoomId
            result = PoTokenResult("", visitorData, pot, pot)
            isRestored = true
            Log.d(TAG, "Restored TV poToken from disk, valid for ${(savedExpirationMs - System.currentTimeMillis()) / 60_000} min")
            result
        } catch (e: Throwable) {
            Log.e(TAG, "Could not restore the TV poToken", e)
            clearSaved()
            null
        }
    }

    private fun save(visitorData: String, pot: String, livingRoomId: String, expirationMs: Long) {
        try {
            prefs()?.edit()
                ?.putString("visitorData", visitorData)
                ?.putString("pot", pot)
                ?.putString("livingRoomId", livingRoomId)
                ?.putLong("expirationMs", expirationMs)
                ?.apply()
        } catch (e: Throwable) {
            Log.e(TAG, "Could not save the TV poToken", e)
        }
    }

    private fun clearSaved() {
        try {
            prefs()?.edit()?.clear()?.apply()
        } catch (e: Throwable) {
            Log.e(TAG, "Could not clear the TV poToken", e)
        }
    }
}