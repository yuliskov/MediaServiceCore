package com.liskovsoft.youtubeapi.app.potokennp2

import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.AppApi
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenGenerator
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenResult
import com.liskovsoft.youtubeapi.app.potokennp2.generators.TclPoTokenWebView4
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
    private val api by lazy { RetrofitHelper.create(AppApi::class.java) }
    private var generator: PoTokenGenerator? = null
    private var result: PoTokenResult? = null

    /**
     * The id that should be passed to the player request: tvAppInfo.livingRoomPoTokenId
     */
    var livingRoomPoTokenId: String? = null
        private set

    val isExpired: Boolean
        get() = generator?.isExpired() ?: true

    fun getPoToken(): PoTokenResult? {
        if (!PoTokenProviderImpl.isWebPotSupported) {
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

    fun reset() {
        synchronized(lock) {
            generator?.let { runCatching { it.close() } }
            generator = null
            result = null
            livingRoomPoTokenId = null
        }
    }

    private fun getPoTokenSync(): PoTokenResult? {
        result?.let { if (!isExpired) return it }

        val tvConfig = RetrofitHelper.get(api.getTvConfig())
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
        val appInfo = RetrofitHelper.get(api.getAppInfo(DefaultHeaders.APP_USER_AGENT))
        val livingRoomId = appInfo?.livingRoomPoTokenId
        val visitorData = appInfo?.visitorData

        if (livingRoomId == null || visitorData == null) {
            Log.e(TAG, "The /tv page doesn't contain the living room token id or the visitor data")
            return null
        }

        reset()

        val newGenerator = TclPoTokenWebView4.newPoTokenGenerator(AppService.instance().context, requestKey, challenge, AppClient.TV)
        val sessionPot = newGenerator.generatePoToken(livingRoomId)

        // Log.d(TAG, "TV poToken: livingRoomId=$livingRoomId, pot=$sessionPot, visitor_data=$visitorData")

        generator = newGenerator
        livingRoomPoTokenId = livingRoomId
        // Same session token is used both for the player request and the streaming
        result = PoTokenResult("", visitorData, sessionPot, sessionPot)

        return result
    }
}