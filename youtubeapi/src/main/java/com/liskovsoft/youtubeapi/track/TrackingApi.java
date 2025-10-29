package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.googlecommon.common.converters.jsonpath.WithJsonPath;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

@WithJsonPath
public interface TrackingApi {
    /**
     * LONG (Marks exact time)<br/>
     * Only creates watch record. This method doesn't update watch time!<br/>
     * Minimal watch record url:<br/>
     * https://www.youtube.com/api/stats/playback?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&vm=CAEQARgEKiB1bnk3SDdPbWtzNlMtcXhfMFlWdjc2Sk5sTm13V19JWjoyQUdiNlo4UDVweWd4ZjMxY1F0S3dmVTdHM2ZYUjJ1Yk02ZjQ3Z3B1c3A4U1lzX0lMbXc
     */
    @GET("https://www.youtube.com/api/stats/playback?ns=yt&ver=2")
    Call<WatchTimeEmptyResult> createWatchRecord(
            @Query("docid") String videoId,              // Video Id
            @Query("len") float lengthSec,               // e.g. 526.91
            @Query("cmt") float watchStartSec,           // e.g. 119.405
            @Query("cpn") String clientPlaybackNonce,    // Client Playback Nonce, unique hash code for each query (see AppService)
            @Query("ei") String eventId,                 // Event Id, ei param from get_video_info
            @Query("vm") String vm,                      // Visitor Monitoring?, vm param from get_video_info
            @Query("of") String of                       // New param. Mandatory.
    );

    /**
     * SHORT (Marks as fully watched)<br/>
     * Only creates watch record. This method doesn't update watch time!<br/>
     * Minimal watch record url:<br/>
     * https://www.youtube.com/api/stats/playback?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&vm=CAEQARgEKiB1bnk3SDdPbWtzNlMtcXhfMFlWdjc2Sk5sTm13V19JWjoyQUdiNlo4UDVweWd4ZjMxY1F0S3dmVTdHM2ZYUjJ1Yk02ZjQ3Z3B1c3A4U1lzX0lMbXc
     */
    @GET("https://www.youtube.com/api/stats/playback?ns=yt&ver=2&final=1")
    Call<WatchTimeEmptyResult> createWatchRecordShort(
            @Query("docid") String videoId,              // Video Id
            @Query("cpn") String clientPlaybackNonce,    // Client Playback Nonce, unique hash code for each query (see AppService)
            @Query("ei") String eventId,                 // Event Id, ei param from get_video_info
            @Query("vm") String vm,                      // Visitor Monitoring?, vm param from get_video_info
            @Query("of") String of                       // New param. Mandatory.
    );

    /**
     * LONG (Marks exact time)<br/>
     * Updates watch time. Doesn't work as standalone method!<br/>
     * In order to work it should be invoked after {@link #createWatchRecordShort}<br/>
     * Minimal watch time url:<br/>
     * https://www.youtube.com/api/stats/watchtime?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&st=71.572&et=71.572
     */
    @GET("https://www.youtube.com/api/stats/watchtime?ns=yt&ver=2")
    Call<WatchTimeEmptyResult> updateWatchTime(
            @Query("docid") String videoId,              // Video Id
            @Query("len") float lengthSec,               // e.g. 526.91
            @Query("st") float jumpFromToSec,            // e.g. 0,119.405 or 119.405
            @Query("et") float jumpFromToSecAlt,         // e.g. 0,119.405 or 119.405
            @Query("cmt") float jumpFromToSecAlt2,       // e.g. 119.405
            @Query("cpn") String clientPlaybackNonce,    // Client Playback Nonce, unique hash code for each query (see AppService)
            @Query("ei") String eventId,                 // Event Id, ei param from get_video_info
            @Query("vm") String vm,                      // Visitor Monitoring?, vm param from get_video_info
            @Query("of") String of                       // New param. Mandatory.
    );

    /**
     * SHORT (Marks as fully watched)<br/>
     * Updates watch time. Doesn't work as standalone method!<br/>
     * In order to work it should be invoked after {@link #createWatchRecordShort}<br/>
     * Minimal watch time url:<br/>
     * https://www.youtube.com/api/stats/watchtime?ns=yt&cpn=gTdSeB6WpIvUxxnP&docid=AgqZaq_IQ8k&ver=2&cmt=71.572&ei=90Q5X5-bOIGZ7ATGuabYAQ&final=1&len=671.0&st=71.572&et=71.572
     */
    @GET("https://www.youtube.com/api/stats/watchtime?ns=yt&ver=2&final=1")
    Call<WatchTimeEmptyResult> updateWatchTimeShort(
            @Query("docid") String videoId,              // Video Id
            @Query("st") float jumpFromToSec,            // e.g. 0,119.405 or 119.405
            @Query("et") float jumpFromToSecAlt,         // e.g. 0,119.405 or 119.405
            @Query("cpn") String clientPlaybackNonce,    // Client Playback Nonce, unique hash code for each query (see AppService)
            @Query("ei") String eventId,                 // Event Id, ei param from get_video_info
            @Query("vm") String vm,                      // Visitor Monitoring?, vm param from get_video_info
            @Query("of") String of                       // New param. Mandatory.
    );
}
