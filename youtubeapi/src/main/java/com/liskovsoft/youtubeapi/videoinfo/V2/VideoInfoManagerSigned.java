package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VideoInfoManagerSigned {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/player")
    Call<VideoInfo> getVideoInfo(@Body String videoQuery, @Header("Authorization") String auth, @Header("x-goog-visitor-id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/player")
    Call<VideoInfo> getVideoInfo(@Body String videoQuery, @Header("Authorization") String auth);

    @Headers({
            "Content-Type: application/json",
            "Origin: https://www.youtube.com",
            "Authorization: " + AppConstants.RESTRICTED_AUTHORIZATION,
            "Cookie: " + AppConstants.RESTRICTED_COOKIE
    })
    @POST("https://www.youtube.com/youtubei/v1/player")
    Call<VideoInfo> getVideoInfoRestricted(@Body String videoQuery);
}
