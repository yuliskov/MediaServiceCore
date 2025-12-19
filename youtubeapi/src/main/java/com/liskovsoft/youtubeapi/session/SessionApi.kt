package com.liskovsoft.youtubeapi.session

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPathSkip
import com.liskovsoft.youtubeapi.session.models.SessionDataResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

@WithJsonPathSkip
internal interface SessionApi {
    @Headers(
        "Accept: */*",
        "Referer: https://www.youtube.com/sw.js"
    )
    @GET("https://www.youtube.com/sw.js_data")
    fun getSessionData(@HeaderMap headers: Map<String, String>): Call<SessionDataResult?>
}