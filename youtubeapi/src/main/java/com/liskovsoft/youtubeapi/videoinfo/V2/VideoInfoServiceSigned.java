package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceSigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceSigned.class.getSimpleName();
    private static VideoInfoServiceSigned sInstance;
    private final VideoInfoApiSigned mVideoInfoApiSigned;

    private VideoInfoServiceSigned() {
        mVideoInfoApiSigned = RetrofitHelper.withJsonPath(VideoInfoApiSigned.class);
    }

    public static VideoInfoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceSigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String authorization) {
        return getVideoInfo(videoId, null, authorization);
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams, String authorization) {
        VideoInfo result = getVideoInfoPrivate(videoId, clickTrackingParams, authorization);

        if (result != null && result.getVideoDetails() != null && result.getVideoDetails().isLive()) {
            Log.e(TAG, "Enable seeking support on the live streams...");
            // Contains dashManifestUrl!!!
            VideoInfo infoLive = getVideoInfoLive(videoId, clickTrackingParams, authorization);
            if (infoLive != null && infoLive.getDashManifestUrl() != null) {
                result.sync(getDashInfo(infoLive.getDashManifestUrl()));
                result.setDashManifestUrl(infoLive.getDashManifestUrl());
            }
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
        String videoInfoQuery = VideoInfoApiParams.getVideoInfoQueryPrivate(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoLive(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoApiParams.getVideoInfoQueryLive(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoEmbed(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoApiParams.getVideoInfoQueryEmbed2(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    /**
     * NOTE: user history won't work with this method
     */
    private VideoInfo getVideoInfoRestricted(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiParams.getVideoInfoQueryRegular(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfoRestricted(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRegular(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoApiParams.getVideoInfoQueryRegular(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }
}
