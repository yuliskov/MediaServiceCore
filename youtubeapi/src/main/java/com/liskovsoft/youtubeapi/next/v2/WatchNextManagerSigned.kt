package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * For signed users!
 */
interface WatchNextManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    fun getWatchNextResult(@Body suggestQuery: String?, @Header("Authorization") auth: String?): Call<WatchNextResult?>?

    //@Headers("Content-Type: application/json")
    //@POST("https://www.youtube.com/youtubei/v1/next")
    //fun continueWatchNextResult(@Body suggestQuery: String?, @Header("Authorization") auth: String?): Call<WatchNextResultContinuation?>?
}