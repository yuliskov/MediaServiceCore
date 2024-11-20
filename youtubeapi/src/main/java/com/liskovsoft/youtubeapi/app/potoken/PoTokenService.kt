package com.liskovsoft.youtubeapi.app.potoken

import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

internal object PoTokenService {
    private const val REQUEST_KEY = "O43z0dpjhgX20SCx4KAo"
    private val appService = AppService.instance()

    @JvmStatic
    fun getChallenge(): Challenge.Result? {
        val challenge = Challenge()

        val result = challenge.create(getConfig())
        return result
    }

    private fun getConfig() = BotGuardConfig(RetrofitHelper.create(PoTokenApi::class.java), requestKey = REQUEST_KEY, identifier = appService.visitorData)
}