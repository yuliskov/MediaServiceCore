package com.liskovsoft.youtubeapi.next;

import com.liskovsoft.youtubeapi.next.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.next.result.WatchNextResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
public interface WatchNextManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<WatchNextResult> getWatchNextResult(@Body String suggestQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/next")
    Call<WatchNextResultContinuation> continueWatchNextResult(@Body String suggestQuery, @Header("Authorization") String auth);
}
