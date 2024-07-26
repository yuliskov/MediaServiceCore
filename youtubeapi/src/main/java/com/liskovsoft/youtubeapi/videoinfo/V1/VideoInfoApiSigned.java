package com.liskovsoft.youtubeapi.videoinfo.V1;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.common.converters.querystring.QueryStringClass;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NOTE: html5=1 is required. Otherwise you'll get 404 error "This is not a P3P policy!"<br/>
 * Unlock Gemini Man 4K: https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted videos: https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
@QueryStringClass
public interface VideoInfoApiSigned {
    // Unused method. sts - ???
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang, @Query("sts") String sts);

    // Unused method
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);

    /**
     * Unlock live hls streams
     */
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfoHls(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);

    /**
     * Unlock age restricted videos
     */
    @GET(AppConstants.GET_VIDEO_INFO_OLD2)
    Call<VideoInfo> getVideoInfoRestricted(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);

    /**
     * Unlock personal videos: <code>c=TVHTML5&cver=7.20201103.00.00</code><br/>
     */
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfoRegular(@Query("video_id") String videoId, @Query("access_token") String token, @Query("hl") String lang);
}
