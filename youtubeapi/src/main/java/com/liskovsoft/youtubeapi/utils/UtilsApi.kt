package com.liskovsoft.youtubeapi.utils

import com.liskovsoft.googlecommon.common.converters.regexp.WithRegExp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

@WithRegExp
internal interface UtilsApi {
    @GET("https://www.youtube.com/{oldChannelId}")
    fun canonicalChannelId(@Path("oldChannelId") altChannelId: String): Call<ChannelId>?
}