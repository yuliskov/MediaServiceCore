package com.liskovsoft.youtubeapi.chat

import com.liskovsoft.youtubeapi.chat.gen.LiveChatResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface LiveChatApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/live_chat/get_live_chat")
    fun getLiveChat(@Body chatQuery: String?): Call<LiveChatResult?>?
}