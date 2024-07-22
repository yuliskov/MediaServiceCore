package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoHls;
import retrofit2.Call;

public class VideoInfoService extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoService.class.getSimpleName();
    private static VideoInfoService sInstance;
    private final VideoInfoApi mVideoInfoApi;
    private final static int VIDEO_INFO_DEFAULT = 0;
    private final static int VIDEO_INFO_ANDROID = 1;
    private final static int VIDEO_INFO_IOS = 2;
    private int mVideoInfoType;

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
        //RetrofitOkHttpHelper.skipAuth();

        VideoInfo result;

        switch (mVideoInfoType) {
            case VIDEO_INFO_ANDROID:
                result = getVideoInfoAndroid(videoId, clickTrackingParams);
                break;
            case VIDEO_INFO_IOS:
                result = getVideoInfoIOS(videoId, clickTrackingParams);
                break;
            default:
                result = getVideoInfoWeb(videoId, clickTrackingParams);
                break;
        }

        // NOTE: Request below doesn't contain dashManifestUrl!!!
        //result = getVideoInfoTV(videoId, clickTrackingParams); // no dash url and hls link
        //result = getVideoInfoAndroid(videoId, clickTrackingParams); // no seek preview, no dash url, fix 403 error?
        //result = getVideoInfoGeoWeb(videoId, clickTrackingParams); // no seek preview, fix 403 error!!
        //result = getVideoInfoWeb(videoId, clickTrackingParams); // all included, the best but many 403 errors(
        //result = getVideoInfoIOS(videoId, clickTrackingParams); // only FullHD, no 403 error?

        //result.sync(getVideoInfoWeb(videoId, clickTrackingParams));

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

    public void applyVideoInfoFix() {
        mVideoInfoType = Helpers.getNextValue(mVideoInfoType, new int[] {VIDEO_INFO_DEFAULT, VIDEO_INFO_ANDROID, VIDEO_INFO_IOS});
    }

    public void invalidateCache() {
        mVideoInfoType = 0;
    }

    /**
     * NOTE: Doesn't contain dash manifest url and hls link
     */
    private VideoInfo getVideoInfoTV(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryTV(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    /**
     * NOTE: Doesn't contain dash manifest url
     */
    private VideoInfo getVideoInfoAndroid(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryAndroid(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfo getVideoInfoEmbed(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryEmbed(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfo getVideoInfoGeoWeb(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryGeoWeb(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfo getVideoInfoIOS(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryIOS(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    private VideoInfoHls getVideoInfoIOSHls(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryIOS(videoId, clickTrackingParams);
        return getVideoInfoHls(videoInfoQuery);
    }

    private VideoInfo getVideoInfoWeb(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryWeb(videoId, clickTrackingParams);
        return getVideoInfo(videoInfoQuery);
    }

    /**
     * NOTE: user history won't work with this method
     */
    private VideoInfo getVideoInfoRestrictedWeb(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryWeb(videoId, clickTrackingParams);

        return getVideoInfoRestricted(videoInfoQuery);
    }

    private VideoInfo getVideoInfo(String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorId());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoWeb(String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfoWeb(videoInfoQuery, mAppService.getVisitorId());

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
        } else if (isExtendedHlsFormatsEnabled() && result.isExtendedHlsFormatsBroken() || result.isStoryboardBroken()) {
            VideoInfoHls videoInfoHls = getVideoInfoIOSHls(videoId, clickTrackingParams);
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
            result = getVideoInfoTV(result.getTrailerVideoId(), clickTrackingParams);
        } else if (result.isUnplayable()) {
            Log.e(TAG, "Found restricted video. Retrying with embed query method...");
            result = getVideoInfoEmbed(videoId, clickTrackingParams);

            if (result == null || result.isUnplayable()) {
                Log.e(TAG, "Found restricted video. Retrying with restricted query method...");
                result = getVideoInfoRestrictedWeb(videoId, clickTrackingParams);

                if (result == null || result.isUnplayable()) {
                    Log.e(TAG, "Found video clip blocked in current location...");
                    result = getVideoInfoGeoWeb(videoId, clickTrackingParams);
                }
            }
        }

        return result;
    }

    private static boolean isExtendedHlsFormatsEnabled() {
        return GlobalPreferences.sInstance != null && GlobalPreferences.sInstance.isExtendedHlsFormatsEnabled();
    }
}
