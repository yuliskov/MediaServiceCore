package com.liskovsoft.youtubeapi.innertube

import com.liskovsoft.googlecommon.common.converters.gson.WithGson
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
        "Referer: https://www.youtube.com",
        "X-Origin: https://www.youtube.com"
    )
    @POST("https://www.youtube.com/youtubei/v1/config") // ${Constants.URLS.API.PRODUCTION_1}v1/config?prettyPrint=false
    fun retrieveInnertubeConfig(@HeaderMap headers: Map<String, String>, @Body jsonConfig: String): Call<InnertubeConfigResult?>
}