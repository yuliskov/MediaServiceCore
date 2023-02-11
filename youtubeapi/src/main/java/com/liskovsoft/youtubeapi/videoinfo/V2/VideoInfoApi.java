package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
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
            "Authorization: SAPISIDHASH 1675699353_5b8a43e8ca59cde3670d9c68756e55d1ce9aea2b",
            "Cookie: __Secure-3PAPISID=rnbVuTkx-ByJTOSi/AGwGjkvI2nm5C5GZu; __Secure-3PSID=TQjMe3u7aPEx2sia_GJA4KKT_99yZ6kyoN3sgFIubVMYRBXE_7Cn2UQ8ejV086jbDTR6mg."
    })
    @POST("https://www.youtube.com/youtubei/v1/player?key=AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8")
    Call<VideoInfo> getVideoInfoRestricted(@Body String videoQuery, @Header("x-goog-visitor-id") String visitorId);
}
