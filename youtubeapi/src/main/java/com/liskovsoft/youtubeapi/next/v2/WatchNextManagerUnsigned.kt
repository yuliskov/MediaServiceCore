package com.liskovsoft.youtubeapi.next.v2

import retrofit2.http.POST
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers

/**
 * For unsigned users!
 */
interface WatchNextManagerUnsigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    fun getWatchNextResult(@Body watchNextQuery: String?): Call<WatchNextResult?>?

//    @Headers("Content-Type: application/json")
//    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
//    fun continueWatchNextResult(@Body watchNextQuery: String?, @Header("X-Goog-Visitor-Id") visitorId: String?): Call<WatchNextResultContinuation?>?
//
//    @Headers("Content-Type: application/json")
//    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
//    fun continueWatchNextResult(@Body watchNextQuery: String?): Call<WatchNextResultContinuation?>?
}