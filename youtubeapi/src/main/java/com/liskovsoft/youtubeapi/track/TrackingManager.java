package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.track.models.WatchTimeResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface TrackingManager {
    /**
     * NOTE: doesn't work anymore<br/>
     * Minimal watch time url:<br/>
     * https://www.youtube.com/api/stats/watchtime?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&st=71.572&et=71.572
     */
    @GET("https://www.youtube.com/api/stats/watchtime?ns=yt&ver=2&final=1")
    Call<WatchTimeResult> updateWatchTime(
            @Query("docid") String videoId,
            @Query("len") float lengthSec,               // e.g. 526.91
            @Query("cmt") float positionSec,             // e.g. 119.405
            @Query("st") float jumpToPositionSec,        // e.g. 0,119.405 or 119.405
            @Query("et") float jumpToPositionAltSec,     // e.g. 0,119.405 or 119.405
            @Query("cpn") String clientPlaybackNonce,    // generated code for each query (see AppService)
            @Query("ei") String eventId,                 // ei param from get_video_info
            @Header("Authorization") String auth
    );

    /**
     * Minimal watch time url:<br/>
     * https://www.youtube.com/api/stats/playback?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&vm=CAEQARgEKiB1bnk3SDdPbWtzNlMtcXhfMFlWdjc2Sk5sTm13V19JWjoyQUdiNlo4UDVweWd4ZjMxY1F0S3dmVTdHM2ZYUjJ1Yk02ZjQ3Z3B1c3A4U1lzX0lMbXc
     */
    @GET("https://www.youtube.com/api/stats/playback?ns=yt&ver=2") // final - ???
    Call<WatchTimeResult> updateWatchTime2(
            @Query("docid") String videoId,
            @Query("len") float lengthSec,               // e.g. 526.91
            @Query("cmt") float positionSec,             // e.g. 119.405
            @Query("cpn") String clientPlaybackNonce,    // generated code for each query (see AppService)
            @Query("ei") String eventId,                 // ei param from get_video_info
            @Query("vm") String vm,                      // ???
            @Header("Authorization") String auth
    );
}
