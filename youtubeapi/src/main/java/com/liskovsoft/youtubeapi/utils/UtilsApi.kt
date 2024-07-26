package com.liskovsoft.youtubeapi.utils

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExpClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

@RegExpClass
internal interface UtilsApi {
    @GET("https://www.youtube.com/{oldChannelId}")
    fun canonicalChannelId(@Path("oldChannelId") altChannelId: String): Call<ChannelId>?
}