package com.liskovsoft.youtubeapi.innertube.ytcfg

import com.liskovsoft.googlecommon.common.converters.regexp.WithRegExp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

@WithRegExp
internal interface YtCfgApi {
    @Headers("Referer: https://www.reddit.com/")
    @GET
    fun getYtCfg(@Url url: String, @Header("User-Agent") userAgent: String): Call<YtCfgResult?>
}