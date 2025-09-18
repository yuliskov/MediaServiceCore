package com.liskovsoft.youtubeapi.app

import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp2.PoTokenProviderImpl
import com.liskovsoft.youtubeapi.app.potokennp2.misc.PoTokenResult

internal object PoTokenGate {
    private var mWebPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1
    
    @JvmStatic
    fun getWebContentPoToken(videoId: String): String? {
        if (mWebPoToken?.videoId == videoId && !PoTokenProviderImpl.isWebPotExpired) {
            return mWebPoToken?.playerRequestPoToken
        }

        mWebPoToken = if (PoTokenProviderImpl.isWebPotSupported)
            PoTokenProviderImpl.getWebClientPoToken(videoId)
        else null

        return mWebPoToken?.playerRequestPoToken
    }

    @JvmStatic
    fun getSessionPoToken(): String? {
        return if (PoTokenProviderImpl.isWebPotSupported) {
            if (mWebPoToken == null)
                mWebPoToken = PoTokenProviderImpl.getWebClientPoToken("")
            mWebPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }

    @JvmStatic
    fun updatePoToken() {
        if (PoTokenProviderImpl.isWebPotSupported) {
            //mNpPoToken = null // only refresh
            mWebPoToken = PoTokenProviderImpl.getWebClientPoToken("") // refresh and preload
        } else {
            PoTokenCloudService.updatePoToken()
        }
    }

    @JvmStatic
    fun getVisitorData(): String? {
        return mWebPoToken?.visitorData
    }

    @JvmStatic
    fun isWebPotSupported() = PoTokenProviderImpl.isWebPotSupported

    @JvmStatic
    fun isWebPotExpired() = PoTokenProviderImpl.isWebPotExpired

    @JvmStatic
    fun resetCache(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        if (currentTimeMs < mCacheResetTimeMs)
            return false

        if (PoTokenProviderImpl.isWebPotSupported) {
            mWebPoToken = null
        } else
            PoTokenCloudService.resetCache()

        mCacheResetTimeMs = currentTimeMs + 60_000

        return true
    }
}