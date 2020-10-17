package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.next.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.next.result.WatchNextResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For unsigned users!
 */
public interface WatchNextManagerUnsigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    Call<WatchNextResult> getWatchNextResult(@Body String watchNextQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next?key=" + AppConstants.API_KEY)
    Call<WatchNextResultContinuation> continueWatchNextResult(@Body String watchNextQuery, @Header("X-Goog-Visitor-Id") String visitorId);
}
