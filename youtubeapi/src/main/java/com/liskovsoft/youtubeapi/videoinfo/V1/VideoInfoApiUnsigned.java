package com.liskovsoft.youtubeapi.videoinfo.V1;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NOTE: html5=1 is required. Otherwise you'll get 404 error "This is not a P3P policy!"<br/>
 * Unlock unavailable videos: https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted videos: https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
public interface VideoInfoApiUnsigned {
    // Unused method. sts - ???
    @GET("https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION_TV)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("hl") String lang, @Query("sts") String sts);
    
    @GET("https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION_TV)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("hl") String lang);

    /**
     * Good for live translations
     */
    @GET("https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION_TV)
    Call<VideoInfo> getVideoInfoHls(@Query("video_id") String videoId, @Query("hl") String lang);

    /**
     * Good for age restricted videos
     */
    @GET("https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=" + AppConstants.CLIENT_VERSION_TV)
    Call<VideoInfo> getVideoInfoRestricted(@Query("video_id") String videoId, @Query("hl") String lang);
}
