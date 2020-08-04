package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface VideoInfoManager {
    @Headers("Accept-Encoding: gzip, deflate, br")
    @GET("https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfo(@Query("video_id") String videoId, @Query("sts") String sts);

    @Headers("Accept-Encoding: gzip, deflate, br")
    @GET("https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv")
    Call<VideoInfoResult> getVideoInfo(@Query("video_id") String videoId);
}
