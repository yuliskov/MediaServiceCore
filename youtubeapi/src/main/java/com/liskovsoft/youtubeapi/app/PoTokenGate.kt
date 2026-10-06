package com.liskovsoft.youtubeapi.app

import com.liskovsoft.youtubeapi.app.potoken.PoTokenService
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp2.WebPoTokenProvider
import com.liskovsoft.youtubeapi.app.potokennp2.TvPoTokenProvider
import com.liskovsoft.youtubeapi.app.potokennp2.core.PoTokenResult
import com.liskovsoft.youtubeapi.app.potokennp2.misc.selectFactory
import com.liskovsoft.youtubeapi.common.helpers.AppClient

/**
 * PoTokenType
 *
 * `CONTENT` A poToken generated from videoId.
 * Used in DASH/SABR requests (e.g. `pot` param).
 * Previously used in player requests.
 *
 * `SESSION` A poToken generated from visitorData.
 * Usage is unknown. Previously used in DASH/SABR requests (e.g. `pot` param).
 */
internal object PoTokenGate {
    private var mWebPoToken: PoTokenResult? = null
    private var mTvPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1
    private var mTvCacheResetTimeMs: Long = -1
    private const val CACHE_RESET_TIME_MS = 60_000

    init {
        WebPoTokenProvider.poTokenFactory = selectFactory()
    }

    private fun getWebContentPoToken(videoId: String): String? {
        if (mWebPoToken?.videoId == videoId && !WebPoTokenProvider.isWebPotExpired) {
            return mWebPoToken?.playerRequestPoToken
        }

        mWebPoToken = if (WebPoTokenProvider.isWebPotSupported)
            WebPoTokenProvider.getWebClientPoToken(videoId)
        else null

        return mWebPoToken?.playerRequestPoToken
    }

    private fun getWebSessionPoToken(): String? {
        return if (WebPoTokenProvider.isWebPotSupported) {
            if (mWebPoToken == null)
                mWebPoToken = WebPoTokenProvider.getWebClientPoToken("")
            mWebPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }

    /**
     * The living room (TV) client uses its own BotGuard config (request key and challenge from tv_config)
     */
    private fun getTvPoToken(): String? {
        if (mTvPoToken == null || TvPoTokenProvider.isExpired) {
            mTvPoToken = TvPoTokenProvider.getPoToken()
        }

        // The TV poToken is a session one (not bound to the video id)
        return mTvPoToken?.playerRequestPoToken
    }

    /**
     * Should be passed to the player request: tvAppInfo.livingRoomPoTokenId
     */
    @JvmStatic
    fun getLivingRoomPoTokenId(client: AppClient): String? =
        if (client.isTvPotRequired) TvPoTokenProvider.livingRoomPoTokenId else null

    @JvmStatic
    @JvmOverloads
    fun getPoToken(client: AppClient, videoId: String? = null): String? {
        resetOtherProviders(client)
        return when {
            client.isWebPotRequired -> if (videoId != null) getWebContentPoToken(videoId) else getWebSessionPoToken()
            client.isTvPotRequired -> getTvPoToken()
            else -> null
        }
    }

    @JvmStatic
    fun getColdStartPoToken(client: AppClient, videoId: String): String? =
        if (client.isWebPotRequired) PoTokenService.generateColdStartToken(videoId) else null

    @JvmStatic
    fun getVisitorData(client: AppClient): String? {
        return when {
            client.isWebPotRequired -> mWebPoToken?.visitorData
            client.isTvPotRequired -> mTvPoToken?.visitorData
            else -> null
        }
    }

    @JvmStatic
    fun isPotSupported() = WebPoTokenProvider.isWebPotSupported

    @JvmStatic
    fun isPotExpired(client: AppClient): Boolean {
        return when {
            client.isWebPotRequired -> WebPoTokenProvider.isWebPotExpired
            client.isTvPotRequired -> TvPoTokenProvider.isExpired
            else -> false
        }
    }

    @JvmStatic
    fun resetCache(client: AppClient): Boolean {
        return when {
            client.isWebPotRequired -> resetWebCache()
            client.isTvPotRequired -> resetTvCache()
            else -> false
        }
    }

    @JvmStatic
    fun resetCache() {
        resetWebCache()
        resetTvCache()
    }

    private fun resetWebCache(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        if (currentTimeMs < mCacheResetTimeMs)
            return false

        if (WebPoTokenProvider.isWebPotSupported) {
            mWebPoToken = null
            WebPoTokenProvider.reset()
        } else
            PoTokenCloudService.resetCache()

        mCacheResetTimeMs = currentTimeMs + CACHE_RESET_TIME_MS

        return true
    }

    private fun resetTvCache(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        if (currentTimeMs < mTvCacheResetTimeMs)
            return false

        mTvPoToken = null
        TvPoTokenProvider.reset()

        mTvCacheResetTimeMs = currentTimeMs + CACHE_RESET_TIME_MS

        return true
    }

    private fun resetOtherProviders(client: AppClient) {
        when {
            client.isWebPotRequired -> TvPoTokenProvider.reset()
            client.isTvPotRequired -> WebPoTokenProvider.reset()
        }
    }
}