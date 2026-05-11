package com.liskovsoft.youtubeapi.app.potokencloud2

import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

@WithGson
internal interface PoTokenCloudApi {
    @GET
    fun getPoToken(@Url url: String, @Query("content_binding") contentBinding: String): Call<PoTokenResponse?>

    // /health-check
    @GET
    fun healthCheck(@Url url: String): Call<Void>
}