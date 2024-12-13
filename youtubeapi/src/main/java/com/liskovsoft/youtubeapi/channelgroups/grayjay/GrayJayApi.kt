package com.liskovsoft.youtubeapi.channelgroups.grayjay

import com.liskovsoft.youtubeapi.channelgroups.grayjay.gen.GrayJayGroup
import com.liskovsoft.youtubeapi.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

@WithGson
internal interface GrayJayApi {
    @GET
    fun getChannelGroups(@Url fileUrl: String): Call<List<GrayJayGroup?>?>?
}