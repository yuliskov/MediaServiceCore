package com.liskovsoft.youtubeapi.next.v2.mock

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.liskovsoft.youtubeapi.next.v2.WatchNextApi
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface WatchNextApiMock: WatchNextApi {
    @Mock
    @MockResponse(body = "next/v2/no_suggestions3.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String): Call<WatchNextResult?>

    @Mock
    @MockResponse(body = "next/v2/no_suggestions3.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String, @Header("X-Goog-Visitor-Id") visitorId: String): Call<WatchNextResult?>
}

internal interface WatchNextApiMock2: WatchNextApi {
    @Mock
    @MockResponse(body = "next/v2/no_suggestions5.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String): Call<WatchNextResult?>

    @Mock
    @MockResponse(body = "next/v2/no_suggestions5.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String, @Header("X-Goog-Visitor-Id") visitorId: String): Call<WatchNextResult?>
}

internal interface WatchNextApiMock3: WatchNextApi {
    @Mock
    @MockResponse(body = "next/v2/no_suggestions6.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String): Call<WatchNextResult?>

    @Mock
    @MockResponse(body = "next/v2/no_suggestions6.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String, @Header("X-Goog-Visitor-Id") visitorId: String): Call<WatchNextResult?>
}