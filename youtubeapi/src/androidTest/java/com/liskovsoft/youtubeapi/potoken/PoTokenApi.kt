package com.liskovsoft.youtubeapi.potoken

import com.liskovsoft.youtubeapi.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://jnn-pa.googleapis.com"
private const val GOOG_API_KEY = "AIzaSyDyT5W0Jh49F30Pqqtyfdf7pDLFKLJoAnw"
private const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36(KHTML, like Gecko)"

@WithGson
internal interface PoTokenApi {
    @Headers(
        "Content-Type: application/json+protobuf",
        "User-Agent: $USER_AGENT",
        "X-Goog-Api-Key: $GOOG_API_KEY",
        "X-User-Agent: grpc-web-javascript/0.1"
    )
    @POST("$BASE_URL/\$rpc/google.internal.waa.v1.Waa/Create")
    fun createChallenge(@Body jsonPayload: String?): Call<List<String?>?>
}