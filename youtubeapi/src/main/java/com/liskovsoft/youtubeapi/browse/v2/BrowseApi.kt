package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.browse.v2.gen.BrowseResult
import com.liskovsoft.youtubeapi.browse.v2.gen.ContinuationResult
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BrowseApi {
    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_CHROME
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getBrowseResult(@Body browseQuery: String?): Call<BrowseResult?>?

    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_CHROME
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getContinuationResult(@Body continuationQuery: String?): Call<ContinuationResult?>?
}