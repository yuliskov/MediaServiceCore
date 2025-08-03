package com.liskovsoft.youtubeapi.browse.v2.mock

import co.infinum.retromock.meta.Mock
import co.infinum.retromock.meta.MockResponse
import com.liskovsoft.youtubeapi.browse.v2.BrowseApi
import com.liskovsoft.youtubeapi.browse.v2.gen.BrowseResultTV
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

internal interface BrowseApiMock: BrowseApi {
    @Mock
    @MockResponse(body = "browse/tv/2025.04.06_home.json")
    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    override fun getBrowseResultTV(browseQuery: String?): Call<BrowseResultTV?>

    @Mock
    @MockResponse(body = "browse/tv/2025.07.25_home_continuation.json")
    @Headers(
        "Content-Type: application/json",
        "User-Agent: " + DefaultHeaders.USER_AGENT_TV,
        "Referer: https://www.youtube.com/tv"
    )
    @POST("https://www.youtube.com/youtubei/v1/browse")
    override fun getContinuationResultTV(@Body continuationQuery: String?): Call<WatchNextResultContinuation?>
}