package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.youtubeapi.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

@WithGson
internal interface PoTokenCloudApi {
    @GET("$PO_TOKEN_CLOUD_BASE_URL/")
    fun getPoToken(@Query("visitorData") visitorData: String? = null): Call<PoTokenResponse?>?
    
    @GET("$PO_TOKEN_CLOUD_BASE_URL/health-check")
    fun healthCheck(): Call<Void>
}