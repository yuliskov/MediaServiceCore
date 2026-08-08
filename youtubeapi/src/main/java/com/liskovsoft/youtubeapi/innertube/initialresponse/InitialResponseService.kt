package com.liskovsoft.youtubeapi.innertube.initialresponse

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathConverterFactory
import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathResponseBodyConverter
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.js.JSInterpret
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.okhttp.OkHttpManager
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.innertube.models.PlayerResult
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo
import java.util.regex.Pattern

internal object InitialResponseService {
    private val YT_INITIAL_PLAYER_RESPONSE_RE: Pattern = Pattern.compile("""ytInitialPlayerResponse\s*=""")

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun getVideoInfo(videoId: String, auth: Boolean = false): VideoInfo? {
        getPlayerResponse(videoId, auth)?.let {
            val factory = JsonPathConverterFactory.create()
            val converter = factory.responseBodyConverter(VideoInfo::class.java, null, null)
            converter as JsonPathResponseBodyConverter<VideoInfo>
            return converter.convert(Helpers.toStream(it))
        }

        return null
    }

    fun getPlayerResult(videoId: String, auth: Boolean = false): PlayerResult? {
        getPlayerResponse(videoId, auth)?.let {
            val gson = Gson()
            val playerResultType = object : TypeToken<PlayerResult>() {}.type

            return try {
                gson.fromJson(it, playerResultType)
            } catch (_: JsonSyntaxException) {
                null
            }
        }

        return null
    }

    private fun getPlayerResponse(videoId: String, auth: Boolean = false): String? {
        val result = makeRequest("https://www.youtube.com/watch?v=$videoId&bpctr=9999999999&has_verified=1", auth)

        return result?.let {
            JSInterpret.searchJson(YT_INITIAL_PLAYER_RESPONSE_RE, it)
        }
    }

    private fun makeRequest(
        url: String,
        auth: Boolean
    ): String? {
        val response = OkHttpManager.instance().doGetRequest(
            url,
            if (auth) RetrofitOkHttpHelper.authHeaders else null
        )

        return if (response.isSuccessful) response.body()?.string() else null
    }
}