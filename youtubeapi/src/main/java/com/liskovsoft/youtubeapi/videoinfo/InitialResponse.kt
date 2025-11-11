package com.liskovsoft.youtubeapi.videoinfo

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathConverterFactory
import com.liskovsoft.googlecommon.common.converters.jsonpath.converter.JsonPathResponseBodyConverter
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.js.JSInterpret
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo
import java.util.regex.Pattern

internal object InitialResponse {
    private val YT_INITIAL_PLAYER_RESPONSE_RE: Pattern = Pattern.compile("""ytInitialPlayerResponse\s*=""")

    @Suppress("UNCHECKED_CAST")
    @JvmStatic
    fun getVideoInfo(videoId: String, auth: Boolean = true): VideoInfo? {
        val fileApi = RetrofitHelper.create(FileApi::class.java)
        val resultWrapper = fileApi.getContent("https://www.youtube.com/watch?v=$videoId")
        val result = RetrofitHelper.get(resultWrapper, auth)

        result?.content?.let {
            val jsonStr = JSInterpret.searchJson(YT_INITIAL_PLAYER_RESPONSE_RE, it)

            val factory = JsonPathConverterFactory.create()
            val converter = factory.responseBodyConverter(VideoInfo::class.java, null, null)
            converter as JsonPathResponseBodyConverter<VideoInfo>
            return converter.convert(Helpers.toStream(jsonStr))
        }

        return null
    }
}