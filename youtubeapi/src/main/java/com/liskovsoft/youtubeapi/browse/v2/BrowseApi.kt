package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@WithGson
internal interface BrowseApi {
    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "Referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getBrowseResult(@Body browseQuery: String?): Call<BrowseResult?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_MOBILE_WEB,
        "Referer: https://m.youtube.com/"
    )
    @POST("https://m.youtube.com/youtubei/v1/browse")
    fun getBrowseResultMobile(@Body browseQuery: String?): Call<BrowseResult?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv/kids"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getBrowseResultKids(@Body browseQuery: String?): Call<BrowseResultKids?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getBrowseResultTV(@Body browseQuery: String?): Call<BrowseResultTV?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "Referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getContinuationResult(@Body continuationQuery: String?): Call<ContinuationResult?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getContinuationResultTV(@Body continuationQuery: String?): Call<WatchNextResultContinuation?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "Referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/guide")
    fun getGuideResult(@Body guideQuery: String?): Call<GuideResult?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv"
    )
    @POST("https://www.youtube.com/youtubei/v1/guide")
    fun getGuideResultTV(@Body guideQuery: String?): Call<GuideResult?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "Referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/reel/reel_item_watch")
    fun getReelResult(@Body reelQuery: String?): Call<ReelResult?>

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_WEB,
        "Referer: https://www.youtube.com/"
    )
    @POST("https://www.youtube.com/youtubei/v1/reel/reel_watch_sequence")
    fun getReelContinuationResult(@Body reelQuery: String?): Call<ReelContinuationResult?>
}