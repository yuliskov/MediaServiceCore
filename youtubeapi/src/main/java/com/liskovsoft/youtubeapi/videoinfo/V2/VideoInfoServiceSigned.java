package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceSigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceSigned.class.getSimpleName();
    private static VideoInfoServiceSigned sInstance;
    private final VideoInfoManagerSigned mVideoInfoManagerSigned;

    private VideoInfoServiceSigned() {
        mVideoInfoManagerSigned = RetrofitHelper.withJsonPath(VideoInfoManagerSigned.class);
    }

    public static VideoInfoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceSigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams, String authorization) {
        VideoInfo result = getVideoInfoPrivate(videoId, clickTrackingParams, authorization);

        if (result != null && result.getVideoDetails() != null && result.getVideoDetails().isLive()) {
            Log.e(TAG, "Enable seeking support on the live streams...");
            result = getVideoInfoLive(videoId, clickTrackingParams, authorization);
        } else if (result != null && result.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfoPrivate(result.getTrailerVideoId(), clickTrackingParams, authorization);
        } else if (result != null && result.isUnplayable()) {
            Log.e(TAG, "Found restricted video. Retrying with embed query method...");
            result = getVideoInfoEmbed(videoId, clickTrackingParams, authorization);

            if (result != null && result.isUnplayable()) {
                Log.e(TAG, "Found restricted video. Retrying with restricted query method...");
                result = getVideoInfoRestricted(videoId, clickTrackingParams);
            }
        }

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s, authorization: %s", videoId, authorization);
        }

        return result;
    }

    private VideoInfo getVideoInfoPrivate(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryPrivate(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoLive(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryLive(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoEmbed(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryEmbed2(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    /**
     * NOTE: user history won't work with this method
     */
    private VideoInfo getVideoInfoRestricted(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryRegular(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfoRestricted(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRegular(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryRegular(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }
}
