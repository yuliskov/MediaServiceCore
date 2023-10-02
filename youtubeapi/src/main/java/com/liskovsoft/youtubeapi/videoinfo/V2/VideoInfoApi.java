package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoHls;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface VideoInfoApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/player")
    Call<VideoInfo> getVideoInfo(@Body String videoQuery, @Header("x-goog-visitor-id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/player")
    Call<VideoInfoHls> getVideoInfoHls(@Body String videoQuery, @Header("x-goog-visitor-id") String visitorId);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/player")
    Call<VideoInfo> getVideoInfo(@Body String videoQuery);

    /**
     * History is not working with this method.<br/>
     * Authorization need to be obtained from the current user credentials.<br/>
     * <a href="https://github.com/zerodytrash/Simple-YouTube-Age-Restriction-Bypass">More info</a><br/>
     * <a href="https://github.com/zerodytrash/Simple-YouTube-Age-Restriction-Bypass/raw/main/dist/Simple-YouTube-Age-Restriction-Bypass.user.js">Sample script</a>
     */
    @Headers({
            "Content-Type: application/json",
            "Origin: https://www.youtube.com",
            "Authorization: SAPISIDHASH 1696275539_755156c48283f6c558adbc9426b6239faae35786",
            "Cookie: __Secure-3PAPISID=3WBOtCxNTBfzDHEV/AgVzT6oyUUxzZIlnF; __Secure-3PSID=bQjMe6x_XmVbeWSGHHvG1K92uzKGe7TAGLeOEerKk-tvp6_3HyXHOT6-EFl_3Kvdg5IJnQ."
    })
    @POST("https://www.youtube.com/youtubei/v1/player?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
    Call<VideoInfo> getVideoInfoRestricted(@Body String videoQuery, @Header("x-goog-visitor-id") String visitorId);

    @Headers({
            "Content-Type: application/json",
            "User-Agent: " + DefaultHeaders.USER_AGENT_WEB
    })
    @POST("https://www.youtube.com/youtubei/v1/player?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
    Call<VideoInfo> getVideoInfoRegular(@Body String videoQuery, @Header("x-goog-visitor-id") String visitorId);
}
