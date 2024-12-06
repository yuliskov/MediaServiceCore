package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.youtubeapi.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

@WithGson
internal interface PoTokenCloudApi {
    @GET("$PO_TOKEN_CLOUD_BASE_URL/")
    fun getPoToken(@Query("visitorData") visitorData: String? = null): Call<PoTokenResponse?>?

    @GET("$PO_TOKEN_CLOUD_BASE_URL2/")
    fun getPoToken2(@Query("visitorData") visitorData: String? = null): Call<PoTokenResponse?>?

    @GET("$PO_TOKEN_CLOUD_BASE_URL/alt")
    fun getPoTokenAlt(@Query("program") program: String): Call<PoTokenResponse?>?

    @GET("$PO_TOKEN_CLOUD_BASE_URL/health-check")
    fun healthCheck(): Call<Void>

    @GET("$PO_TOKEN_CLOUD_BASE_URL2/part1")
    fun getPoTokenPart1(): Call<PoTokenPart1Response?>?

    @Headers("Content-Type: application/json")
    @POST("$PO_TOKEN_CLOUD_BASE_URL2/part2")
    fun getPoTokenPart2(@Body poTokenPart2Query: String): Call<PoTokenResponse?>?
}