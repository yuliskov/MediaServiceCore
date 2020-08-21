package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface VideoInfoManagerSigned {
    // sts - ???
    @GET("https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfo(@Query("video_id") String videoId, @Query("sts") String sts, @Header("Authorization") String auth);
    
    @GET("https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfo(@Query("video_id") String videoId, @Header("Authorization") String auth);
}
