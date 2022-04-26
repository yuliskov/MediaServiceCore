package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceUnsigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceUnsigned.class.getSimpleName();
    private static VideoInfoServiceUnsigned sInstance;
    private final VideoInfoManagerUnsigned mVideoInfoManagerUnsigned;

    private VideoInfoServiceUnsigned() {
        mVideoInfoManagerUnsigned = RetrofitHelper.withJsonPath(VideoInfoManagerUnsigned.class);
    }

    public static VideoInfoServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceUnsigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        VideoInfo result = getVideoInfoRegular(videoId, clickTrackingParams);

        if (result != null && result.getVideoDetails() != null && result.getVideoDetails().isLive()) {
            Log.e(TAG, "Enable seeking support on the live streams...");
            result = getVideoInfoLive(videoId, clickTrackingParams);
        } else if (result != null && result.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfoRegular(result.getTrailerVideoId(), clickTrackingParams);
        } else if (result != null && result.isUnplayable()) {
            Log.e(TAG, "Found restricted video. Retrying with different query method...");
            result = getVideoInfoEmbed(videoId, clickTrackingParams);

            if (result != null && result.isUnplayable()) {
                Log.e(TAG, "Found restricted video. Retrying with restricted query method...");
                result = getVideoInfoRestricted(videoId, clickTrackingParams);
            }
        }

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
        }

        return result;
    }

    private VideoInfo getVideoInfoLive(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryLive(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerUnsigned.getVideoInfo(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoEmbed(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryEmbed2(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerUnsigned.getVideoInfo(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryRegular(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerUnsigned.getVideoInfoRestricted(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRegular(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryLive(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerUnsigned.getVideoInfo(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }
}
