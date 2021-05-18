package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NOTE: html5=1 is required. Otherwise you'll get 404 error "This is not a P3P policy!"<br/>
 * Unlock Gemini Man 4K: https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted videos: https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
public interface VideoInfoManagerSignedV3 {
    // Unused method. sts - ???
    @GET("https://www.youtube.com/get_video_info?html5=1&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("access_token") String token, @Query("sts") String sts);

    // Unused method
    @GET("https://www.youtube.com/get_video_info?html5=1&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("access_token") String token);

    /**
     * Unlock live hls streams
     */
    @GET("https://www.youtube.com/get_video_info?html5=1&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfoHls(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang, @Query("cpn") String cpn);

    /**
     * Unlock age restricted videos
     */
    @GET("https://www.youtube.com/get_video_info?html5=1&ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfoRestricted(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang, @Query("cpn") String cpn);

    /**
     * Unlock personal videos: <code>c=TVHTML5&cver=7.20201103.00.00</code><br/>
     */
    @GET("https://www.youtube.com/get_video_info?html5=1&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&c=TVHTML5&cver=" + AppConstants.CLIENT_VERSION)
    Call<VideoInfo> getVideoInfoRegular(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang, @Query("cpn") String cpn);
}
