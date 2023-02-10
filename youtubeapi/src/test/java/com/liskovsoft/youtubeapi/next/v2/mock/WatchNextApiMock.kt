package com.liskovsoft.youtubeapi.next.v2.mock

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.liskovsoft.youtubeapi.next.v2.WatchNextApi
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface WatchNextApiMock: WatchNextApi {
    @Mock
    @MockResponse(body = "next/v2/no_suggestions3.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String?): Call<WatchNextResult?>?
}

interface WatchNextApiMock2: WatchNextApi {
    @Mock
    @MockResponse(body = "next/v2/no_suggestions5.json")
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    override fun getWatchNextResult(@Body watchNextQuery: String?): Call<WatchNextResult?>?
}