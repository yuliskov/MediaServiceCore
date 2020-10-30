package com.liskovsoft.youtubeapi.track.V2;

import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TrackingManager {
    @GET("https://www.youtube.com/api/stats/playback?ns=yt&ver=2&docid=4oO-X3RkeLk&st=100&et=100&cmt=0&len=508&final=1")
    Call<WatchTimeEmptyResult> createWatchRecord(
            @Query("cpn") String clientPlaybackNonce,
            @Query("ei") String eventId,
            @Query("vm") String vm,                      
            @Header("Authorization") String auth
    );

    @GET("https://www.youtube.com/api/stats/watchtime?ns=yt&ver=2&docid=4oO-X3RkeLk&st=100&et=100&cmt=0&len=508&final=1")
    Call<WatchTimeEmptyResult> updateWatchTime(
            @Query("cpn") String clientPlaybackNonce,
            @Query("ei") String eventId,
            @Query("vm") String vm,
            @Header("Authorization") String auth
    );
}
