package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Unlock Gemini Man 4K: https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted videos: https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
public interface VideoInfoManagerSigned {
    // Unused method. sts - ???
    @GET("https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfo(@Query("video_id") String videoId, @Query("sts") String sts, @Header("Authorization") String auth);
    
    @GET("https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfo(@Query("video_id") String videoId, @Header("Authorization") String auth);

    @GET("https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfoRestricted(@Query("video_id") String videoId, @Header("Authorization") String auth);
}
