package com.liskovsoft.youtubeapi.innertube.core

import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPathSkip
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.innertube.utils.ApiHelpers
import com.liskovsoft.youtubeapi.innertube.utils.URLS
import com.liskovsoft.youtubeapi.innertube.models.InnertubeConfigResult
import com.liskovsoft.youtubeapi.innertube.models.InnertubeContext
import com.liskovsoft.youtubeapi.innertube.models.SessionArgs
import com.liskovsoft.youtubeapi.innertube.models.SessionData
import com.liskovsoft.youtubeapi.innertube.models.SessionDataResult
import com.liskovsoft.youtubeapi.innertube.utils.CLIENTS
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

@WithJsonPathSkip
private interface SessionApi {
    @Headers(
        "Accept: */*",
        "Referer: ${URLS.YT_BASE}/sw.js"
    )
    @GET("${URLS.YT_BASE}/sw.js_data")
    fun getSessionData(@HeaderMap headers: Map<String, String>): Call<SessionDataResult?>
}

@WithGson
private interface InnertubeConfigApi {
    @Headers(
        "Content-Type: application/json",
        "Accept: */*",
        "Referer: ${URLS.YT_BASE}",
        "X-Origin: ${URLS.YT_BASE}"
    )
    @POST("${URLS.API.PRODUCTION_1}v1/config")
    fun retrieveInnertubeConfig(@HeaderMap headers: Map<String, String>, @Body jsonConfig: String): Call<InnertubeConfigResult?>
}

internal class Session private constructor(
    val context: InnertubeContext,
    val apiKey: String,
    val apiVersion: String,
    val accountIndex: Int,
    val configData: String?,
    val userAgent: String,
    val player: Player,
    val cookie: String? = null,
    //cache?: ICache,
    val poToken: String? = null
) {
    companion object {
        fun create(options: SessionOptions? = null): Session? {
            val (apiKey, apiVersion, configData, context, userAgent, accountIndex) = getSessionData(options) ?: return null

            return Session(context, apiKey, apiVersion, accountIndex, configData, userAgent, Player.create(options?.poToken, options?.playerId))
        }

        fun getSessionData(options: SessionOptions? = null): SessionData? {
            // TODO: add caching of session data

            val sessionData = getSessionDataResult() ?: return null

            val args = SessionArgs()

            val context = buildContext(sessionData, args) ?: return null

            val innertubeConfig = retrieveInnertubeConfig(sessionData, context)
            val coldConfigData = innertubeConfig?.responseContext?.globalConfigGroup?.rawColdConfigGroup?.configData
            val coldHashData = innertubeConfig?.responseContext?.globalConfigGroup?.coldHashData
            val hotHashData = innertubeConfig?.responseContext?.globalConfigGroup?.hotHashData
            val configData = innertubeConfig?.configData

            // TODO: store session data

            return SessionData(
                sessionData.apiKey ?: return null,
                CLIENTS.WEB.API_VERSION,
                configData ?: return null,
                context.apply {
                    client.configInfo?.coldConfigData = coldConfigData ?: return null
                    client.configInfo?.coldHashData = coldHashData ?: return null
                    client.configInfo?.hotHashData = hotHashData ?: return null
                }
            )
        }

        fun getSessionDataResult(): SessionDataResult? {
            val sessionApi = RetrofitHelper.create(SessionApi::class.java)
            val sessionDataResult = sessionApi.getSessionData(ApiHelpers.createSessionDataHeaders())
            return RetrofitHelper.get(sessionDataResult, false)
        }

        fun retrieveInnertubeConfig(sessionData: SessionDataResult, context: InnertubeContext): InnertubeConfigResult? {
            val innertubeConfigApi = RetrofitHelper.create(InnertubeConfigApi::class.java)

            val innertubeConfigResult =
                innertubeConfigApi.retrieveInnertubeConfig(
                    ApiHelpers.createInnertubeConfigHeaders(sessionData),
                    ApiHelpers.createInnertubeJsonConfig(context)
                )
            return RetrofitHelper.get(innertubeConfigResult, false)
        }

        private fun buildContext(sessionData: SessionDataResult, options: SessionArgs): InnertubeContext? {
            val deviceInfo = sessionData.deviceInfo ?: return null
            return InnertubeContext(options, deviceInfo)
        }
    }
}

internal data class SessionOptions(
    val lang: String?,
    val location: String?,
    val userAgent: String?,
    val poToken: String?,
    val playerId: String?,
    val retrieveInnertubeConfig: Boolean = true,
    val accountIndex: Int = 0
    // .....
)