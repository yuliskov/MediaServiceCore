package com.liskovsoft.youtubeapi.app.potoken

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal object PoTokenService {
    private const val REQUEST_KEY = "O43z0dpjhgX20SCx4KAo"
    private val appService = AppService.instance()
    
    data class Data(val integrityToken: String?,
                    val estimatedTtlSecs: Int?,
                    val mintRefreshThreshold: Int?,
                    val webSafeFallbackToken: String?)

    data class BotGuardResult(val integrityTokenData: Data?, val postProcessFunction: String?)

    @JvmStatic
    fun getChallenge(): Challenge.Result? {
        val challenge = Challenge()

        val result = challenge.create(getConfig())
        return result
    }

    @JvmStatic
    fun generateIntegrityToken(requestKey: String, botguardResponse: String): BotGuardResult? {
        val wrapper = getConfig().api.generateIntegrityToken(Gson().toJson(arrayOf(requestKey, botguardResponse)))

        val response = RetrofitHelper.get(wrapper) ?: return null

        val integrityToken = response.getOrNull(0)
        val estimatedTtlSecs = response.getOrNull(1)
        val mintRefreshThreshold = response.getOrNull(2)
        val websafeFallbackToken = response.getOrNull(3)

        return BotGuardResult(
            Data(asString(integrityToken), asInt(estimatedTtlSecs), asInt(mintRefreshThreshold), asString(websafeFallbackToken)), null
        )
    }

    private fun getConfig() = BotGuardConfig(RetrofitHelper.create(PoTokenApi::class.java), requestKey = REQUEST_KEY, identifier = appService.visitorData)

    private fun asInt(intElem: JsonElement?) = if (intElem?.isJsonNull == true) null else intElem?.asInt

    private fun asString(strElem: JsonElement?) = if (strElem?.isJsonNull == true) null else strElem?.asString
}