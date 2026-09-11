package com.liskovsoft.youtubeapi.app

import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.potoken.PoTokenService
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp2.PoTokenProviderImpl
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
    private const val TAG = "PoTokenGate"
    private var mWebPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1
    private const val CACHE_RESET_TIME_MS = 60_000

    init {
        PoTokenProviderImpl.poTokenFactory = selectFactory()
    }

    private fun getWebContentPoToken(videoId: String): String? {
        if (PoTokenProviderImpl.isWebPotSupported) {
            if (mWebPoToken?.videoId == videoId && !PoTokenProviderImpl.isWebPotExpired) {
                return mWebPoToken?.playerRequestPoToken
            }

            try {
                PoTokenProviderImpl.getWebClientPoToken(videoId)?.let {
                    mWebPoToken = it
                    return it.playerRequestPoToken
                }
            } catch (e: Exception) {
                Log.e(TAG, "Can't mint local web poToken", e)
            }
        }

        // No local minter (no WebView on the device or it's broken).
        // NOTE: the cloud minter isn't used here on purpose: its endpoints are
        // placeholder URLs, so each call would just stall the playback thread.
        return null
    }

    private fun getWebSessionPoToken(): String? {
        if (PoTokenProviderImpl.isWebPotSupported) {
            try {
                if (mWebPoToken == null)
                    mWebPoToken = PoTokenProviderImpl.getWebClientPoToken("")
                mWebPoToken?.streamingDataPoToken?.let { return it }
            } catch (e: Exception) {
                Log.e(TAG, "Can't mint local web poToken", e)
            }
        }

        // NOTE: cached value only (the cloud endpoints are placeholders).
        return PoTokenCloudService.getPoToken()
    }

    /**
     * Refreshes the web poToken in the background.
     * Call it on the app start (before any playback) to avoid
     * the WebView/BotGuard boot delay on the first video.
     */
    @JvmStatic
    fun updatePoToken() {
        if (PoTokenProviderImpl.isWebPotSupported) {
            mWebPoToken = try {
                PoTokenProviderImpl.getWebClientPoToken("") // refresh and preload
            } catch (e: Exception) {
                Log.e(TAG, "Can't preload web poToken...", e)
                null
            }
        } else {
            try {
                PoTokenCloudService.updatePoToken()
            } catch (e: Exception) {
                Log.e(TAG, "Can't preload cloud web poToken...", e)
            }
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getPoToken(client: AppClient, videoId: String? = null): String? {
        return when {
            client.isWebPotRequired || client.isTVClient -> if (videoId != null) getWebContentPoToken(videoId) else getWebSessionPoToken()
            else -> null
        }
    }

    /**
     * Content poToken used in DASH/SABR stream requests.
     *
     * Only web-based clients require it (see yt-dlp PO Token Guide).
     * TVHTML5 clients must NOT receive it: a pot bound to the web client
     * injected into a TVHTML5-signed SABR URL makes gvs return HTTP 403.
     */
    @JvmStatic
    @JvmOverloads
    fun getStreamPoToken(client: AppClient, videoId: String? = null): String? {
        return when {
            client.isWebPotRequired -> if (videoId != null) getWebContentPoToken(videoId) else getWebSessionPoToken()
            else -> null
        }
    }

    @JvmStatic
    fun getColdStartPoToken(client: AppClient, videoId: String): String? =
        if (client.isWebPotRequired) PoTokenService.generateColdStartToken(videoId) else null

    @JvmStatic
    fun getVisitorData(client: AppClient): String? {
        return when {
            client.isWebPotRequired || client.isTVClient -> getWebVisitorData()
            else -> null
        }
    }

    @JvmStatic
    fun isWebPotSupported() = PoTokenProviderImpl.isWebPotSupported

    @JvmStatic
    fun isWebPotExpired() = PoTokenProviderImpl.isWebPotExpired

    @JvmStatic
    fun resetCache(client: AppClient): Boolean {
        return when {
            client.isWebPotRequired -> resetWebCache()
            else -> false
        }
    }

    @JvmStatic
    fun resetCache() {
        resetWebCache()
    }

    fun getWebVisitorData(): String? {
        return mWebPoToken?.visitorData ?: AppService.instance().visitorData
    }

    private fun resetWebCache(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        if (currentTimeMs < mCacheResetTimeMs)
            return false

        if (PoTokenProviderImpl.isWebPotSupported) {
            mWebPoToken = null
            PoTokenProviderImpl.resetCache()
        } else
            PoTokenCloudService.resetCache()

        mCacheResetTimeMs = currentTimeMs + CACHE_RESET_TIME_MS

        return true
    }
}