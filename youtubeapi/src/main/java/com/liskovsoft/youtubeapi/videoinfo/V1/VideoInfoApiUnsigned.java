package com.liskovsoft.youtubeapi.videoinfo.V1;

import com.liskovsoft.youtubeapi.common.helpers.AppConstants;
import com.liskovsoft.googlecommon.common.converters.querystring.WithQueryString;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NOTE: html5=1 is required. Otherwise you'll get 404 error "This is not a P3P policy!"<br/>
 * Unlock unavailable videos: https://www.youtube.com/get_video_info?ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv<br/>
 * Unlock age restricted videos: https://www.youtube.com/get_video_info?ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv
 */
@WithQueryString
public interface VideoInfoApiUnsigned {
    // Unused method. sts - ???
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("hl") String lang, @Query("sts") String sts, @Query("cver") String clientVersion);
    
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfo(@Query("video_id") String videoId, @Query("hl") String lang, @Query("cver") String clientVersion);

    /**
     * Good for live translations
     */
    @GET(AppConstants.GET_VIDEO_INFO_OLD)
    Call<VideoInfo> getVideoInfoHls(@Query("video_id") String videoId, @Query("hl") String lang, @Query("cver") String clientVersion);

    /**
     * Good for age restricted videos
     */
    @GET(AppConstants.GET_VIDEO_INFO_OLD2)
    Call<VideoInfo> getVideoInfoRestricted(@Query("video_id") String videoId, @Query("hl") String lang, @Query("cver") String clientVersion);
}
