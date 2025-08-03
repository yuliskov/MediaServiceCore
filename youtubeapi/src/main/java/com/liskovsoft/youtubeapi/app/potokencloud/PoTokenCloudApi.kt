package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url

@WithGson
internal interface PoTokenCloudApi {
    @GET
    fun getPoToken(@Url url: String, @Query("visitorData") visitorData: String? = null): Call<PoTokenResponse?>?

    // /health-check
    @GET
    fun healthCheck(@Url url: String): Call<Void>

    // /part1
    @GET
    fun getPoTokenPart1(@Url url: String): Call<PoTokenPart1Response?>?

    // /part2
    @Headers("Content-Type: application/json")
    @POST
    fun getPoTokenPart2(@Url url: String, @Body poTokenPart2Query: String): Call<PoTokenResponse?>?
}