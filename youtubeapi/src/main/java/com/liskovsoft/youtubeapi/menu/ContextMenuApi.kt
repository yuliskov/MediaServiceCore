package com.liskovsoft.youtubeapi.menu

import com.google.gson.JsonObject
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@WithGson
internal interface ContextMenuApi {
    @Headers("Content-Type: application/json", "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv")
    @POST("https://www.youtube.com/youtubei/v1/get_panel")
    fun getPanel(@Body query: String): Call<JsonObject>
}
