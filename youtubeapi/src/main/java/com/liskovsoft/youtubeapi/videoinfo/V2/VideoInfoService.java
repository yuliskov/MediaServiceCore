package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoHls;
import retrofit2.Call;

public class VideoInfoService extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoService.class.getSimpleName();
    private static VideoInfoService sInstance;
    private final VideoInfoApi mVideoInfoApi;

    private VideoInfoService() {
        mVideoInfoApi = RetrofitHelper.withJsonPath(VideoInfoApi.class);
    }

    public static VideoInfoService instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoService();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        // NOTE: Request below doesn't contain dashManifestUrl!!!
        //VideoInfo result = getVideoInfoPrivate(videoId, clickTrackingParams); // no dash url and hls link
        //VideoInfo result = getVideoInfoLive(videoId, clickTrackingParams); // no seek preview, no dash url, fix 403 error?
        VideoInfo result = getVideoInfoGeoBlocked(videoId, clickTrackingParams); // no seek preview, fix 403 error?
        //VideoInfo result = getVideoInfoRegular(videoId, clickTrackingParams); // all included, the best

        applyFixesIfNeeded(result, videoId, clickTrackingParams);
        result = retryIfNeeded(result, videoId, clickTrackingParams);

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
        }

        return result;
    }

    /**
     * NOTE: Doesn't contain dash manifest url and hls link
     */
    private VideoInfo getVideoInfoPrivate(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryPrivate(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    /**
     * NOTE: Doesn't contain dash manifest url
     */
    private VideoInfo getVideoInfoLive(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryLive(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfo getVideoInfoEmbed(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryEmbed2(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfo getVideoInfoGeoBlocked(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryGeoBlocked(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfoHls getVideoInfoHls(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryHls(videoId, clickTrackingParams);
        return getVideoInfoHls(videoInfoQuery);
    }

    private VideoInfo getVideoInfoRegular(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryRegular(videoId, clickTrackingParams);
        return getVideoInfoRegular(videoInfoQuery);
    }

    /**
     * NOTE: user history won't work with this method
     */
    private VideoInfo getVideoInfoRestricted(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryRegular(videoId, clickTrackingParams);

        return getVideoInfoRestricted(videoInfoQuery);
    }

    private VideoInfo getVideoInfo(String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRegular(String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfoRegular(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfoRestricted(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfoHls getVideoInfoHls(String videoInfoQuery) {
        Call<VideoInfoHls> wrapper = mVideoInfoApi.getVideoInfoHls(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private void applyFixesIfNeeded(VideoInfo result, String videoId, String clickTrackingParams) {
        if (result == null || result.isUnplayable()) {
            return;
        }

        if (result.isLive()) {
            Log.e(TAG, "Enable seeking support on live streams...");
            result.sync(getDashInfo2(result));

            // Add dash and hls manifests (for backward compatibility)
            //if (YouTubeMediaService.instance().isOldStreamsEnabled()) {
            //    VideoInfo result2 = getVideoInfoLive(videoId, clickTrackingParams);
            //    result.setDashManifestUrl(result2.getDashManifestUrl());
            //    result.setHlsManifestUrl(result2.getHlsManifestUrl());
            //}
        } else if (result.isExtendedHlsFormatsBroken() || result.isStoryboardBroken()) {
            VideoInfoHls videoInfoHls = getVideoInfoHls(videoId, clickTrackingParams);
            if (videoInfoHls != null && result.getHlsManifestUrl() == null) {
                result.setHlsManifestUrl(videoInfoHls.getHlsManifestUrl());
            }
            if (videoInfoHls != null && result.getStoryboardSpec() == null) {
                result.setStoryboardSpec(videoInfoHls.getStoryboardSpec());
            }
        }
    }

    private VideoInfo retryIfNeeded(VideoInfo result, String videoId, String clickTrackingParams) {
        if (result == null) {
            return null;
        }

        if (result.isUnplayable() && result.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfoPrivate(result.getTrailerVideoId(), clickTrackingParams);
        } else if (result.isUnplayable()) {
            Log.e(TAG, "Found restricted video. Retrying with embed query method...");
            result = getVideoInfoEmbed(videoId, clickTrackingParams);

            if (result != null && result.isUnplayable()) {
                Log.e(TAG, "Found restricted video. Retrying with restricted query method...");
                result = getVideoInfoRestricted(videoId, clickTrackingParams);

                if (result != null && result.isUnplayable()) {
                    Log.e(TAG, "Found video clip blocked in current location...");
                    result = getVideoInfoGeoBlocked(videoId, clickTrackingParams);
                }
            }
        }

        return result;
    }
}
