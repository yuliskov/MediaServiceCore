package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.youtubeapi.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

@WithGson
internal interface PoTokenCloudApi {
    @Headers(
        "Content-Type: application/json",
    )
    @GET("https://potokenservice.onrender.com/")
    fun getPoToken(@Query("visitorData") visitorData: String? = null): Call<PoTokenResponse?>?

    @Headers(
        "Content-Type: application/json",
    )
    @GET("https://potokenservice.onrender.com/health-check")
    fun healthCheck(): Call<Void>
}