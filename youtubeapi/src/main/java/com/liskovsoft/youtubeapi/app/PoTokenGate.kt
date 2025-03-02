package com.liskovsoft.youtubeapi.app

import android.annotation.TargetApi
import android.os.Build
import android.os.Build.VERSION
import com.liskovsoft.sharedutils.helpers.DeviceHelpers
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp.PoTokenProviderImpl
import com.liskovsoft.youtubeapi.app.potokennp.misc.PoTokenResult

internal object PoTokenGate {
    private var npPoToken: PoTokenResult? = null
    private var mCacheResetTimeMs: Long = -1

    @TargetApi(19)
    @JvmStatic
    fun getContentPoToken(videoId: String): String? {
        if (npPoToken?.videoId == videoId) {
            return npPoToken?.playerRequestPoToken
        }

        npPoToken = if (supportsNpPot())
            PoTokenProviderImpl.getWebClientPoToken(videoId)
        else null

        return npPoToken?.playerRequestPoToken
    }

    @JvmStatic
    fun getSessionPoToken(): String? {
        return if (supportsNpPot()) {
            if (npPoToken == null)
                npPoToken = PoTokenProviderImpl.getWebClientPoToken("")
            npPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }

    @JvmStatic
    fun updatePoToken() {
        if (supportsNpPot()) {
            if (npPoToken == null)
                npPoToken = PoTokenProviderImpl.getWebClientPoToken("")
        } else {
            PoTokenCloudService.updatePoToken()
        }
    }

    @JvmStatic
    fun getVisitorData(): String? {
        return npPoToken?.visitorData
    }

    @JvmStatic
    fun supportsNpPot() = VERSION.SDK_INT >= 19 && DeviceHelpers.supportsWebView() && !isWebViewBroken()

    private fun isWebViewBroken(): Boolean = VERSION.SDK_INT == 19 && Build.BRAND.startsWith("TCL") // "TCL TV - Harman"

    @TargetApi(19)
    @JvmStatic
    fun resetCache(): Boolean {
        if (System.currentTimeMillis() < mCacheResetTimeMs)
            return false

        if (supportsNpPot()) {
            npPoToken = null
            PoTokenProviderImpl.resetCache()
        } else
            PoTokenCloudService.resetCache()

        mCacheResetTimeMs = System.currentTimeMillis() + 60_000

        return true
    }
}