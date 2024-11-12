package com.liskovsoft.youtubeapi.videoinfo.V2;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
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
    private boolean mSkipAuth;
    private boolean mSkipAuthBlock;

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
        mSkipAuthBlock = mSkipAuth;

        VideoInfo result = getRootVideoInfo(videoId, clickTrackingParams);

        mSkipAuthBlock = false;

        if (result == null) {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
            return null;
        }

        if (mSkipAuth) {
            result.sync(getRootVideoInfo(videoId, clickTrackingParams));
        }

        result = retryIfNeeded(result, videoId, clickTrackingParams);

        mSkipAuthBlock = result.isHistoryBroken();

        applyFixesIfNeeded(result, videoId, clickTrackingParams);

        mSkipAuthBlock = false;

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
        return getVideoInfo(mVideoInfoType, videoId, clickTrackingParams);
    }

    private VideoInfo getVideoInfo(int type, String videoId, String clickTrackingParams) {
        VideoInfo result = null;

        switch (type) {
            case VIDEO_INFO_INITIAL:
                result = InitialResponse.getVideoInfo(videoId, mSkipAuthBlock);
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
        resetData();
        restoreVideoInfoType();
    }

    public void fixPlaybackErrors() {
        MediaServiceData.instance().enableFormat(MediaServiceData.FORMATS_EXTENDED_HLS, false);
        nextVideoInfo();
        persistVideoInfoType();
    }

    public void resetInfoType() {
        resetData();
        persistVideoInfoType();
    }

    private void resetData() {
        mVideoInfoType = -1;
        mSkipAuth = false;
        nextVideoInfo();
    }

    private void nextVideoInfo() {
        boolean defaultValue = true;

        if (mVideoInfoType != -1 && mSkipAuth == defaultValue) {
            mSkipAuth = !defaultValue;
            return;
        }

        mVideoInfoType = Helpers.getNextValue(mVideoInfoType, VIDEO_INFO_TYPE_LIST);
        mSkipAuth = defaultValue;
    }

    private VideoInfo getVideoInfo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfoGeo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryGeo(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    /**
     * NOTE: user history won't work with this method
     */
    private VideoInfo getVideoInfoRestricted(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(client, videoId, clickTrackingParams);

        return getVideoInfoRestricted(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfo(AppClient client, String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorData(), client != null ? client.getUserAgent() : null);

        return getVideoInfo(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(AppClient client, String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfoRestricted(videoInfoQuery, mAppService.getVisitorData(), client != null ? client.getUserAgent() : null);

        return getVideoInfo(wrapper);
    }

    private @Nullable VideoInfo getVideoInfo(Call<VideoInfo> wrapper) {
        VideoInfo videoInfo = RetrofitHelper.get(wrapper, mSkipAuthBlock);

        if (videoInfo != null && mSkipAuthBlock) {
            videoInfo.setHistoryBroken(true);
        }

        return videoInfo;
    }

    private VideoInfoHls getVideoInfoIOSHls(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(AppClient.IOS, videoId, clickTrackingParams);
        return getVideoInfoHls(videoInfoQuery);
    }

    private VideoInfoHls getVideoInfoHls(String videoInfoQuery) {
        Call<VideoInfoHls> wrapper = mVideoInfoApi.getVideoInfoHls(videoInfoQuery, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper, mSkipAuthBlock);
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

        if (shouldObtainExtendedFormats(result) || result.isStoryboardBroken()) {
            Log.d(TAG, "Enable high bitrate formats...");
            VideoInfoHls videoInfoHls = getVideoInfoIOSHls(videoId, clickTrackingParams);
            if (videoInfoHls != null && shouldObtainExtendedFormats(result)) {
                result.setHlsManifestUrl(videoInfoHls.getHlsManifestUrl());
            }
            if (videoInfoHls != null && result.isStoryboardBroken()) {
                result.setStoryboardSpec(videoInfoHls.getStoryboardSpec());
            }
        }

        // TV and others has a limited number of auto generated subtitles
        if (result.getTranslationLanguages() != null && result.getTranslationLanguages().size() < 50) {
            Log.d(TAG, "Enable full list of auto generated subtitles...");
            mSkipAuthBlock = true;
            VideoInfo webInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
            mSkipAuthBlock = false;
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
                        mSkipAuthBlock = !mSkipAuth;
                        VideoInfo rootResult = getRootVideoInfo(videoId, clickTrackingParams);
                        mSkipAuthBlock = false;

                        if (rootResult == null || rootResult.isUnplayable()) {
                            return null;
                        }
                        return rootResult;
                    }
            );

            //if (result == null || result.isUnplayable()) {
            //    result = getFirstPlayableByType(videoId, clickTrackingParams);
            //}
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

        for (int type : VIDEO_INFO_TYPE_LIST) {
            mSkipAuthBlock = true;
            VideoInfo videoInfo = getVideoInfo(type, videoId, clickTrackingParams);
            mSkipAuthBlock = false;

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

        Pair<Integer, Boolean> videoInfoType = data.getVideoInfoType();
        if (videoInfoType != null && videoInfoType.first != -1) {
            mVideoInfoType = videoInfoType.first;
            mSkipAuth = videoInfoType.second;
        }
    }

    private void persistVideoInfoType() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        MediaServiceData data = MediaServiceData.instance();
        data.setVideoInfoType(mVideoInfoType, mSkipAuth);
    }

    private static boolean shouldObtainExtendedFormats(VideoInfo result) {
        return MediaServiceData.instance().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS) && result.isExtendedHlsFormatsBroken();
    }
}
