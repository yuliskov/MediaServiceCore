package com.liskovsoft.youtubeapi.innertube.api

import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import com.liskovsoft.youtubeapi.innertube.helpers.URLS
import com.liskovsoft.youtubeapi.innertube.models.InnertubeConfigResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

@WithGson
internal interface InnertubeConfigApi {
    @Headers(
        "Content-Type: application/json",
        "Accept: */*",
        "Referer: ${URLS.YT_BASE}",
        "X-Origin: ${URLS.YT_BASE}"
    )
    @POST("${URLS.API.PRODUCTION_1}v1/config")
    fun retrieveInnertubeConfig(@HeaderMap headers: Map<String, String>, @Body jsonConfig: String): Call<InnertubeConfigResult?>
}