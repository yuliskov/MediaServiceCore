package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;
import com.liskovsoft.youtubeapi.videoinfo.InitialResponse;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoHls;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.List;

import retrofit2.Call;

public class VideoInfoService extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoService.class.getSimpleName();
    private static VideoInfoService sInstance;
    private final VideoInfoApi mVideoInfoApi;
    private final static int VIDEO_INFO_INITIAL = 0;
    private final static int VIDEO_INFO_WEB = 1;
    private final static int VIDEO_INFO_MWEB = 2;
    private final static int VIDEO_INFO_TV = 3;
    private final static int VIDEO_INFO_ANDROID = 4;
    private final static int VIDEO_INFO_IOS = 5;
    private final static int VIDEO_INFO_EMBED = 6;
    private final static int[] VIDEO_INFO_TYPE_LIST = {
            VIDEO_INFO_TV, VIDEO_INFO_IOS, VIDEO_INFO_EMBED, VIDEO_INFO_MWEB, VIDEO_INFO_ANDROID, VIDEO_INFO_INITIAL, VIDEO_INFO_WEB
    };
    private int mVideoInfoType = -1;

    private interface VideoInfoCallback {
        VideoInfo call();
    }

    private VideoInfoService() {
        mVideoInfoApi = RetrofitHelper.create(VideoInfoApi.class);
        initVideoInfo();
    }

    public static VideoInfoService instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoService();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        //RetrofitOkHttpHelper.skipAuth();

        VideoInfo result = getRootVideoInfo(videoId, clickTrackingParams);

        if (result == null) {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
            return null;
        }

        //result.sync(getRootVideoInfo(videoId, clickTrackingParams));

        applyFixesIfNeeded(result, videoId, clickTrackingParams);
        result = retryIfNeeded(result, videoId, clickTrackingParams);

        List<AdaptiveVideoFormat> adaptiveFormats = null;
        List<RegularVideoFormat> regularFormats = null;

        if (MediaServiceData.instance().isFormatEnabled(MediaServiceData.FORMATS_DASH) || result.getRegularFormats() == null) {
            decipherFormats(result.getAdaptiveFormats());
            adaptiveFormats = result.getAdaptiveFormats();
        }

        if (MediaServiceData.instance().isFormatEnabled(MediaServiceData.FORMATS_URL) || result.getAdaptiveFormats() == null) {
            decipherFormats(result.getRegularFormats());
            regularFormats = result.getRegularFormats();
        }

        result.setAdaptiveFormats(adaptiveFormats);
        result.setRegularFormats(regularFormats);

        return result;
    }

    private VideoInfo getRootVideoInfo(String videoId, String clickTrackingParams) {
        VideoInfo result = null;

        switch (mVideoInfoType) {
            case VIDEO_INFO_INITIAL:
                result = InitialResponse.getVideoInfo(videoId);
                if (result != null) {
                    VideoInfo syncInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
                    result.sync(syncInfo);
                    break;
                }
            case VIDEO_INFO_TV:
                // Doesn't contain dash manifest url and hls link
                // Support viewing private (user) videos
                result = getVideoInfo(AppClient.TV, videoId, clickTrackingParams);
                break;
            case VIDEO_INFO_WEB:
                result = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
                break;
            case VIDEO_INFO_MWEB:
                result = getVideoInfo(AppClient.MWEB, videoId, clickTrackingParams);
                break;
            case VIDEO_INFO_ANDROID:
                // Doesn't contain dash manifest url
                result = getVideoInfo(AppClient.ANDROID, videoId, clickTrackingParams);
                break;
            case VIDEO_INFO_IOS:
                result = getVideoInfo(AppClient.IOS, videoId, clickTrackingParams);
                break;
            case VIDEO_INFO_EMBED:
                result = getVideoInfo(AppClient.EMBED, videoId, clickTrackingParams);
                break;
        }

        return result;
    }

    private void initVideoInfo() {
        mVideoInfoType = -1;
        nextVideoInfo();
        restoreVideoInfoType();
    }

    public void fixVideoInfo() {
        nextVideoInfo();
        persistVideoInfoType();
    }

    public void invalidateCache() {
        mVideoInfoType = -1;
        nextVideoInfo();
        persistVideoInfoType();
    }

    private void nextVideoInfo() {
        mVideoInfoType = Helpers.getNextValue(mVideoInfoType, VIDEO_INFO_TYPE_LIST);
    }

    private VideoInfo getVideoInfo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(videoId, clickTrackingParams, client);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfoGeo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryGeo(videoId, clickTrackingParams, client);
        return getVideoInfo(client, videoInfoQuery);
    }

    /**
     * NOTE: user history won't work with this method
     */
    private VideoInfo getVideoInfoRestricted(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(videoId, clickTrackingParams, client);

        return getVideoInfoRestricted(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfo(AppClient client, String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorId(), client != null ? client.getUserAgent() : null);

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(AppClient client, String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfoRestricted(videoInfoQuery, mAppService.getVisitorId(), client != null ? client.getUserAgent() : null);

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfoHls getVideoInfoIOSHls(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(videoId, clickTrackingParams, AppClient.IOS);
        return getVideoInfoHls(videoInfoQuery);
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
            Log.d(TAG, "Enable seeking support on live streams...");
            result.sync(getDashInfo(result));

            // Add dash and hls manifests (for backward compatibility)
            //if (YouTubeMediaService.instance().isOldStreamsEnabled()) {
            //    VideoInfo result2 = getVideoInfoLive(videoId, clickTrackingParams);
            //    result.setDashManifestUrl(result2.getDashManifestUrl());
            //    result.setHlsManifestUrl(result2.getHlsManifestUrl());
            //}
        }

        if ((MediaServiceData.instance().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS) && result.isExtendedHlsFormatsBroken()) || result.isStoryboardBroken()) {
            Log.d(TAG, "Enable high bitrate formats...");
            VideoInfoHls videoInfoHls = getVideoInfoIOSHls(videoId, clickTrackingParams);
            if (videoInfoHls != null && result.getHlsManifestUrl() == null) {
                result.setHlsManifestUrl(videoInfoHls.getHlsManifestUrl());
            }
            if (videoInfoHls != null && result.getStoryboardSpec() == null) {
                result.setStoryboardSpec(videoInfoHls.getStoryboardSpec());
            }
        }

        // TV and others has a limited number of auto generated subtitles
        if (result.getTranslationLanguages() != null && result.getTranslationLanguages().size() < 50) {
            Log.d(TAG, "Enable full list of auto generated subtitles...");
            VideoInfo webInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
            if (webInfo != null) {
                result.setTranslationLanguages(webInfo.getTranslationLanguages());
            }
        }
    }

    private VideoInfo retryIfNeeded(VideoInfo videoInfo, String videoId, String clickTrackingParams) {
        if (videoInfo == null) {
            return null;
        }

        VideoInfo result = null;

        if (videoInfo.isUnplayable() && videoInfo.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfo(AppClient.TV, videoInfo.getTrailerVideoId(), clickTrackingParams);
        } else if (videoInfo.isUnplayable()) {
            result = getFirstPlayable(
                    () -> getVideoInfo(AppClient.EMBED, videoId, clickTrackingParams), // Restricted (18+) videos
                    //() -> getVideoInfoRestricted(videoId, clickTrackingParams, AppClient.MWEB), // Restricted videos (no history)
                    () -> getVideoInfoGeo(AppClient.WEB, videoId, clickTrackingParams), // Video clip blocked in current location
                    () -> {
                        // Auth users only. The latest bug fix for "This content isn't available".
                        RetrofitOkHttpHelper.skipAuth();
                        VideoInfo rootResult = getRootVideoInfo(videoId, clickTrackingParams);

                        if (rootResult == null || rootResult.isUnplayable()) {
                            return null;
                        }
                        return rootResult;
                    }
            );

            if (result == null || result.isUnplayable()) {
                result = getFirstPlayableByType(videoId, clickTrackingParams);
            }
        }

        return result != null ? result : videoInfo;
    }
    
    private VideoInfo getFirstPlayable(VideoInfoCallback... callbacks) {
        VideoInfo result = null;

        for (VideoInfoCallback callback : callbacks) {
            VideoInfo videoInfo = callback.call();

            if (videoInfo != null && !videoInfo.isUnplayable()) {
                result = videoInfo;
                break;
            }
        }

        return result;
    }

    private VideoInfo getFirstPlayableByType(String videoId, String clickTrackingParams) {
        VideoInfo result = null;

        for (AppClient client : AppClient.values()) {
            RetrofitOkHttpHelper.skipAuth();
            VideoInfo videoInfo = getVideoInfo(client, videoId, clickTrackingParams);

            if (videoInfo != null && !videoInfo.isUnplayable()) {
                 result = videoInfo;
                 break;
            }
        }

        return result;
    }

    private void restoreVideoInfoType() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        MediaServiceData data = MediaServiceData.instance();

        mVideoInfoType = data.getVideoInfoType() != -1 ? data.getVideoInfoType() : mVideoInfoType;
    }

    private void persistVideoInfoType() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        MediaServiceData data = MediaServiceData.instance();
        data.setVideoInfoType(mVideoInfoType);
    }
}
