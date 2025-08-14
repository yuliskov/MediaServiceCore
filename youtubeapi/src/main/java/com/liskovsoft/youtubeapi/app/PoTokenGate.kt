package com.liskovsoft.youtubeapi.app

import android.os.Build.VERSION
import com.liskovsoft.sharedutils.helpers.DeviceHelpers
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp2.PoTokenProviderImpl
import com.liskovsoft.youtubeapi.app.potokennp2.misc.PoTokenResult

internal object PoTokenGate {
    private var mNpPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1
    
    @JvmStatic
    fun getContentPoToken(videoId: String): String? {
        if (mNpPoToken?.videoId == videoId) {
            return mNpPoToken?.playerRequestPoToken
        }

        mNpPoToken = if (isNpPotSupported())
            PoTokenProviderImpl.getWebClientPoToken(videoId)
        else null

        return mNpPoToken?.playerRequestPoToken
    }

    @JvmStatic
    fun getSessionPoToken(): String? {
        return if (isNpPotSupported()) {
            if (mNpPoToken == null)
                mNpPoToken = PoTokenProviderImpl.getWebClientPoToken("")
            mNpPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }

    @JvmStatic
    fun updatePoToken() {
        if (isNpPotSupported()) {
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
    fun isNpPotSupported() = VERSION.SDK_INT >= 19 && DeviceHelpers.isWebViewSupported() && !isWebViewBroken()

    private fun isWebViewBroken(): Boolean = VERSION.SDK_INT == 19 && DeviceHelpers.isTCL() // "TCL TV - Harman"

    @JvmStatic
    fun resetCache(): Boolean {
        val currentTimeMs = System.currentTimeMillis()
        if (currentTimeMs < mCacheResetTimeMs)
            return false

        if (isNpPotSupported()) {
            mNpPoToken = null
        } else
            PoTokenCloudService.resetCache()

        mCacheResetTimeMs = currentTimeMs + 60_000

        return true
    }
}