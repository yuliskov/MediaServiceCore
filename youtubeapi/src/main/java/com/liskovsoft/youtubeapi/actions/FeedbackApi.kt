package com.liskovsoft.youtubeapi.actions

import com.liskovsoft.youtubeapi.actions.models.HideVideoTokenResult
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST

interface FeedbackApi {
    @Headers("Content-Type: application/json", "User-Agent: " + DefaultHeaders.USER_AGENT_CHROME)
    @POST("https://www.youtube.com/youtubei/v1/browse")
    fun getHideVideoToken(videoId: String): Call<HideVideoTokenResult>
}