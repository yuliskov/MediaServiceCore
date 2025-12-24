package com.liskovsoft.youtubeapi.innertube.api

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPathSkip
import com.liskovsoft.youtubeapi.innertube.helpers.URLS
import com.liskovsoft.youtubeapi.innertube.models.SessionDataResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers

@WithJsonPathSkip
internal interface SessionApi {
    @Headers(
        "Accept: */*",
        "Referer: ${URLS.YT_BASE}/sw.js"
    )
    @GET("${URLS.YT_BASE}/sw.js_data")
    fun getSessionData(@HeaderMap headers: Map<String, String>): Call<SessionDataResult?>
}