package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Unlock Gemini Man 4K: https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted videos: https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
public interface VideoInfoManagerSignedV2 {
    // Unused method. sts - ???
    @GET("https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("sts") String sts, @Query("access_token") String auth);
    
    @GET("https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("access_token") String auth);

    /**
     * Unlock live hls streams
     */
    @GET("https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfoHls(@Query("video_id") String videoId, @Query("hl") String lang, @Query("access_token") String auth);

    /**
     * Unlock age restricted videos
     */
    @GET("https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfoRestricted(@Query("video_id") String videoId, @Query("access_token") String auth);

    /**
     * Unlock personal videos: <code>c=TVHTML5&cver=7.20201103.00.00</code><br/>
     */
    @GET("https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&c=TVHTML5&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfoRegular(@Query("video_id") String videoId, @Query("hl") String lang, @Query("access_token") String auth);
}
