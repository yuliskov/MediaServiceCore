package com.liskovsoft.youtubeapi.app

import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp2.PoTokenProviderImpl
import com.liskovsoft.youtubeapi.app.potokennp2.misc.PoTokenResult

internal object PoTokenGate {
    private var mNpPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1
    
    @JvmStatic
    fun getContentPoToken(videoId: String): String? {
        if (mNpPoToken?.videoId == videoId && !PoTokenProviderImpl.isExpired) {
            return mNpPoToken?.playerRequestPoToken
        }

        mNpPoToken = if (PoTokenProviderImpl.isPotSupported)
            PoTokenProviderImpl.getWebClientPoToken(videoId)
        else null

        return mNpPoToken?.playerRequestPoToken
    }

    @JvmStatic
    fun getSessionPoToken(): String? {
        return if (PoTokenProviderImpl.isPotSupported) {
            if (mNpPoToken == null)
                mNpPoToken = PoTokenProviderImpl.getWebClientPoToken("")
            mNpPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }

    @JvmStatic
    fun updatePoToken() {
        if (PoTokenProviderImpl.isPotSupported) {
            //mNpPoToken = null // only refresh
            mNpPoToken = PoTokenProviderImpl.getWebClientPoToken("") // refresh and preload
        } else {
            PoTokenCloudService.updatePoToken()
        }
    }

    @JvmStatic
    fun getVisitorData(): String? {
        return mNpPoToken?.visitorData
    }

    @JvmStatic
    fun isPotSupported() = PoTokenProviderImpl.isPotSupported

    @JvmStatic
    fun isExpired() = PoTokenProviderImpl.isExpired

    @JvmStatic
    fun resetCache(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        if (currentTimeMs < mCacheResetTimeMs)
            return false

        if (PoTokenProviderImpl.isPotSupported) {
            mNpPoToken = null
        } else
            PoTokenCloudService.resetCache()

        mCacheResetTimeMs = currentTimeMs + 60_000

        return true
    }
}