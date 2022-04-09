package com.liskovsoft.youtubeapi.videoinfo.V2;

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

    /**
     * History is not working with this method.<br/>
     * Authorization need to be obtained from the current user credentials.<br/>
     * <a href="https://github.com/zerodytrash/Simple-YouTube-Age-Restriction-Bypass">More info</a><br/>
     * <a href="https://github.com/zerodytrash/Simple-YouTube-Age-Restriction-Bypass/raw/main/dist/Simple-YouTube-Age-Restriction-Bypass.user.js">Sample script</a>
     */
    @Headers({
            "Content-Type: application/json",
            "Origin: https://www.youtube.com",
            "Authorization: SAPISIDHASH 1645062921_df354f673ee52822175a0a9f4813c39eea0f95cc",
            "Cookie: __Secure-3PAPISID=JbTGDgqvynRIJ6ZM/A4LyWqJOh5AEa1zzf; __Secure-3PSID=GgjMe58C83cWIeEgmGKn9cRE2zjRGwRwlmFHwIk2x-O3_0TgTrK-lKpZ6q3MDo-To6Ahgw."
    })
    @POST("https://www.youtube.com/youtubei/v1/player?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
    Call<VideoInfo> getVideoInfoRestricted(@Body String videoQuery, @Header("x-goog-visitor-id") String visitorId);
}
