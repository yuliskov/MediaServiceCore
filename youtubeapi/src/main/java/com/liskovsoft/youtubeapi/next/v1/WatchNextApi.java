package com.liskovsoft.youtubeapi.next.v1;

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
@WithJsonPath
public interface WatchNextApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<WatchNextResult> getWatchNextResult(@Body String watchNextQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<WatchNextResultContinuation> continueWatchNextResult(@Body String watchNextQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<WatchNextResultContinuation> continueWatchNextResult(@Body String suggestQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
