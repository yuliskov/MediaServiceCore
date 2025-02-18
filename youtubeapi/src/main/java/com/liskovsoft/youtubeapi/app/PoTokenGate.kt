package com.liskovsoft.youtubeapi.app

import android.annotation.TargetApi
import android.os.Build.VERSION
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenCloudService
import com.liskovsoft.youtubeapi.app.potokennp.PoTokenProviderImpl
import com.liskovsoft.youtubeapi.app.potokennp.misc.DeviceUtils
import com.liskovsoft.youtubeapi.app.potokennp.misc.PoTokenResult

internal object PoTokenGate {
    private var npPoToken: PoTokenResult? = null

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
                getContentPoToken("any_val_to_init")
            npPoToken?.streamingDataPoToken
        } else PoTokenCloudService.getPoToken()
    }

    @Synchronized
    @JvmStatic
    fun updatePoToken() {
        if (supportsNpPot()) {
            if (npPoToken == null)
                getContentPoToken("any_val_to_init")
        } else {
            PoTokenCloudService.updatePoToken()
        }
    }
    
    private fun supportsNpPot() = false
    //private fun supportsNpPot() = VERSION.SDK_INT >= 19 && DeviceUtils.supportsWebView()
}