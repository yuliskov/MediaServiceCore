package com.liskovsoft.youtubeapi.app

import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp2.PoTokenProviderImpl
import com.liskovsoft.youtubeapi.app.potokennp2.misc.PoTokenResult
import com.liskovsoft.youtubeapi.common.helpers.AppClient

private enum class PoTokenType {
    /**
     * A poToken generated from videoId.
     *
     * Used in player requests.
      */
    CONTENT,

    /**
     * A generic poToken.
     *
     * Used in SABR requests.
     */
    SESSION
}

internal object PoTokenGate {
    private var mWebPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1

    private fun getWebContentPoToken(videoId: String): String? {
        if (mWebPoToken?.videoId == videoId && !PoTokenProviderImpl.isWebPotExpired) {
            return mWebPoToken?.playerRequestPoToken
        }

        mWebPoToken = if (PoTokenProviderImpl.isWebPotSupported)
            PoTokenProviderImpl.getWebClientPoToken(videoId)
        else null

        return mWebPoToken?.playerRequestPoToken
    }

    private fun getWebSessionPoToken(): String? {
        return if (PoTokenProviderImpl.isWebPotSupported) {
            if (mWebPoToken == null)
                mWebPoToken = PoTokenProviderImpl.getWebClientPoToken("")
            mWebPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }
    
    private fun updatePoToken() {
        if (PoTokenProviderImpl.isWebPotSupported) {
            //mNpPoToken = null // only refresh
            mWebPoToken = PoTokenProviderImpl.getWebClientPoToken("") // refresh and preload
        } else {
            PoTokenCloudService.updatePoToken()
        }
    }

    @JvmStatic
    @JvmOverloads
    fun getPoToken(client: AppClient, videoId: String? = null): String? {
        return when {
            client.isWebPotRequired -> if (videoId != null) getWebContentPoToken(videoId) else getWebSessionPoToken()
            else -> null
        }
    }

    @JvmStatic
    fun getVisitorData(client: AppClient): String? {
        return when {
            client.isWebPotRequired -> getWebVisitorData()
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

    fun getWebVisitorData(): String? {
        return mWebPoToken?.visitorData
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

        mCacheResetTimeMs = currentTimeMs + 60_000

        return true
    }
}