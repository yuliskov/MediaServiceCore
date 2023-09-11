package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface BrowseApi {
    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getBrowseResult(@Body browseQuery: String?): Call<BrowseResult?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_MOBILE_WEB,
        "referer: https://m.youtube.com/"
    )
    @POST("https://m.youtube.com/youtubei/v1/browse")
    fun getBrowseResultMobile(@Body browseQuery: String?): Call<BrowseResult?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "referer: https://www.youtube.com/tv/kids"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getBrowseResultKids(@Body browseQuery: String?): Call<BrowseResultKids?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getContinuationResult(@Body continuationQuery: String?): Call<ContinuationResult?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/guide")
    fun getGuideResult(@Body guideQuery: String?): Call<GuideResult?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/reel/reel_item_watch")
    fun getReelResult(@Body reelQuery: String?): Call<ReelResult?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/reel/reel_watch_sequence")
    fun getReelContinuationResult(@Body reelQuery: String?): Call<ReelContinuationResult?>?
}