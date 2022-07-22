package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResultContinuation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface WatchNextApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun getWatchNextResultSigned(@Body suggestQuery: String?, @Header("Authorization") auth: String?): Call<WatchNextResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun getWatchNextResultSigned(@Body suggestQuery: String?, @Header("Authorization") auth: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    fun getWatchNextResultUnsigned(@Body watchNextQuery: String?): Call<WatchNextResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    fun getWatchNextResultUnsigned(@Body watchNextQuery: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun continueWatchNextResultSigned(@Body suggestQuery: String?, @Header("Authorization") auth: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResultContinuation?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    fun continueWatchNextResultUnsigned(@Body watchNextQuery: String?): Call<WatchNextResultContinuation?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    fun continueWatchNextResultUnsigned(@Body watchNextQuery: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResultContinuation?>?
}