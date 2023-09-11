package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.youtubeapi.next.v2.gen.DislikesResult
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import retrofit2.Call
import retrofit2.http.*

internal interface WatchNextApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun getWatchNextResult(@Body watchNextQuery: String?): Call<WatchNextResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun getWatchNextResult(@Body watchNextQuery: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun continueWatchNextResult(@Body watchNextQuery: String?): Call<WatchNextResultContinuation?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun continueWatchNextResult(@Body watchNextQuery: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResultContinuation?>?

    @GET("https://returnyoutubedislikeapi.com/votes")
    fun getDislikes(@Query("videoId") videoId: String?): Call<DislikesResult?>?
}