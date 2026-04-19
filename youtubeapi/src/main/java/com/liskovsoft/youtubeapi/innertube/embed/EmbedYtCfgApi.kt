package com.liskovsoft.youtubeapi.innertube.embed

import com.liskovsoft.googlecommon.common.converters.regexp.WithRegExp
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

@WithRegExp
internal interface EmbedYtCfgApi {
    @Headers(
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "Referer: https://www.reddit.com/")
    @GET("https://www.youtube.com/embed/{videoId}")
    fun getYtCfg(@Path("videoId") videoId: String): Call<YtCfgResult?>
}