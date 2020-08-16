package com.liskovsoft.youtubeapi.track;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TrackingManager {
    /**
     * Minimal watch time url:<br/>
     * https://www.youtube.com/api/stats/watchtime?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&st=71.572&et=71.572
     */
    @GET("https://www.youtube.com/api/stats/watchtime?ns=yt&ver=2&final=1")
    Call<Response<Void>> updateWatchTime(
            @Query("docid") String videoId,
            @Query("cpn") String clientPlaybackNonce,    // specially generated code for each query (see tv-player-ias.js)
            @Query("ei") String eventId,                 // ei param from get_video_info
            @Query("len") float videoLengthSec,         // e.g. 526.91
            @Query("cmt") float positionSec,            // e.g. 119.405
            @Query("st") float jumpFromToSec,           // e.g. 0,119.405 or 119.405
            @Query("et") float jumpFromToSec2,          // e.g. 0,119.405 or 119.405
            @Header("Authorization") String auth);
}
