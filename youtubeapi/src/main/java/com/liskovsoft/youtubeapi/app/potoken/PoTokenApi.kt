package com.liskovsoft.youtubeapi.app.potoken

import com.google.gson.JsonElement
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

private const val BASE_URL = "https://jnn-pa.googleapis.com"
private const val GOOG_API_KEY = "AIzaSyDyT5W0Jh49F30Pqqtyfdf7pDLFKLJoAnw"
private const val USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36(KHTML, like Gecko)"
private const val GRPC_CLIENT = "grpc-web-javascript/0.1"
private const val PROTOBUF_CONTENT = "application/json+protobuf"

@WithGson
internal interface PoTokenApi {
    @Headers(
        "Content-Type: $PROTOBUF_CONTENT",
        "User-Agent: $USER_AGENT",
        "X-Goog-Api-Key: $GOOG_API_KEY",
        "X-User-Agent: $GRPC_CLIENT"
    )
    @POST("$BASE_URL/\$rpc/google.internal.waa.v1.Waa/Create")
    fun createChallenge(@Body jsonPayload: String?): Call<List<String?>?>

    @Headers(
        "Content-Type: $PROTOBUF_CONTENT",
        "User-Agent: $USER_AGENT",
        "X-Goog-Api-Key: $GOOG_API_KEY",
        "X-User-Agent: $GRPC_CLIENT"
    )
    @POST("$BASE_URL/\$rpc/google.internal.waa.v1.Waa/GenerateIT")
    fun generateIntegrityToken(@Body jsonPayload: String?): Call<List<JsonElement?>?>
}