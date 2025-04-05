package com.liskovsoft.youtubeapi.videoinfo.V2;

import android.util.Pair;

import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;
import com.liskovsoft.youtubeapi.videoinfo.InitialResponse;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.TranslationLanguage;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoHls;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.RegularVideoFormat;

import java.util.Arrays;
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
    private final static int WEB_EMBEDDED_PLAYER = 7;
    private final static int ANDROID_VR = 8;
    // VIDEO_INFO_TV can bypass "Sign in to confirm you're not a bot" (rare case)
    // WEB_EMBEDDED_PLAYER - the best one, with no occasional 403 errors
    // VIDEO_INFO_IOS can work without NSig. VIDEO_INFO_TV and VIDEO_INFO_EMBED are the only ones working in North America
    private final static Integer[] VIDEO_INFO_TYPE_LIST = {
            //VIDEO_INFO_TV, VIDEO_INFO_IOS, VIDEO_INFO_EMBED, VIDEO_INFO_MWEB, VIDEO_INFO_ANDROID, VIDEO_INFO_INITIAL, VIDEO_INFO_WEB
            //VIDEO_INFO_WEB, VIDEO_INFO_MWEB, VIDEO_INFO_INITIAL, VIDEO_INFO_IOS, WEB_EMBEDDED_PLAYER, VIDEO_INFO_ANDROID, VIDEO_INFO_TV, VIDEO_INFO_EMBED
            WEB_EMBEDDED_PLAYER, VIDEO_INFO_IOS, VIDEO_INFO_TV, VIDEO_INFO_EMBED
    };
    private int mVideoInfoType = -1;
    private boolean mSkipAuth;
    private boolean mSkipAuthBlock;
    private List<TranslationLanguage> mCachedTranslationLanguages;
    private boolean mIsUnplayable;

    private interface VideoInfoCallback {
        VideoInfo call();
    }

    private VideoInfoService() {
        mVideoInfoApi = RetrofitHelper.create(VideoInfoApi.class);
    }

    public static VideoInfoService instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoService();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        initVideoInfo();

        AppService.instance().resetClientPlaybackNonce(); // unique value per each video info

        mSkipAuthBlock = mSkipAuth;

        VideoInfo result = firstNonNull(videoId, clickTrackingParams);

        mSkipAuthBlock = false;

        if (result == null) {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
            return null;
        }

        mIsUnplayable = result.isUnplayable();

        result = retryIfNeeded(result, videoId, clickTrackingParams);

        mSkipAuthBlock = result.isHistoryBroken();

        applyFixesIfNeeded(result, videoId, clickTrackingParams);

        mSkipAuthBlock = false;

        List<AdaptiveVideoFormat> adaptiveFormats = null;
        List<RegularVideoFormat> regularFormats = null;

        if (MediaServiceData.instance().isFormatEnabled(MediaServiceData.FORMATS_DASH) || !result.containsRegularVideoInfo()) {
            decipherFormats(result.getAdaptiveFormats());
            adaptiveFormats = result.getAdaptiveFormats();
        }

        if (MediaServiceData.instance().isFormatEnabled(MediaServiceData.FORMATS_URL) || !result.containsAdaptiveVideoInfo()) {
            decipherFormats(result.getRegularFormats());
            regularFormats = result.getRegularFormats();
        }

        result.setAdaptiveFormats(adaptiveFormats);
        result.setRegularFormats(regularFormats);

        if (result.isHistoryBroken()) {
            // Only the tv client supports auth features
            result.sync(getVideoInfo(VIDEO_INFO_TV, videoId, clickTrackingParams));
        }

        return result;
    }

    private VideoInfo firstNonNull(String videoId, String clickTrackingParams) {
        final int beginType = Arrays.asList(VIDEO_INFO_TYPE_LIST).contains(mVideoInfoType) ? mVideoInfoType : VIDEO_INFO_TYPE_LIST[0];
        int nextType = beginType;
        VideoInfo result;

        do {
            result = getVideoInfo(nextType, videoId, clickTrackingParams);
            nextType = Helpers.getNextValue(nextType, VIDEO_INFO_TYPE_LIST);
        } while (result == null && nextType != beginType);

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
                    //VideoInfo syncInfo = getVideoInfo(AppClient.TV, videoId, clickTrackingParams);
                    //result.sync(syncInfo);
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
            case WEB_EMBEDDED_PLAYER:
                result = getVideoInfo(AppClient.WEB_EMBEDDED_PLAYER, videoId, clickTrackingParams);
                break;
            case ANDROID_VR:
                result = getVideoInfo(AppClient.ANDROID_VR, videoId, clickTrackingParams);
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
        if (mVideoInfoType != -1) {
            return;
        }

        resetData();
        restoreVideoInfoType();
    }

    public void switchNextFormat() {
        //MediaServiceData.instance().enableFormat(MediaServiceData.FORMATS_EXTENDED_HLS, false); // skip additional formats fetching that produce an error
        if (!mIsUnplayable && isPotSupported(mVideoInfoType) && PoTokenGate.resetCache()) {
            return;
        }
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
        // The same format but without auth may behave better
        if (mVideoInfoType != -1 && !mSkipAuth) {
            mSkipAuth = true;
            return;
        }

        mVideoInfoType = Helpers.getNextValue(mVideoInfoType, VIDEO_INFO_TYPE_LIST);
        mSkipAuth = !isAuthSupported(mVideoInfoType) || MediaServiceData.instance().isPremiumFixEnabled();
    }

    private VideoInfo getVideoInfo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfoGeo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryGeo(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfo(AppClient client, String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorData(), client != null ? client.getUserAgent() : null);

        return getVideoInfo(wrapper, !isAuthSupported(client) || mSkipAuthBlock);
    }

    private @Nullable VideoInfo getVideoInfo(Call<VideoInfo> wrapper, boolean skipAuth) {
        VideoInfo videoInfo = RetrofitHelper.get(wrapper, skipAuth);

        if (videoInfo != null && skipAuth) {
            videoInfo.setHistoryBroken(true);
        }

        return videoInfo;
    }

    private VideoInfoHls getVideoInfoIOSHls(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(AppClient.IOS, videoId, clickTrackingParams);
        return getVideoInfoHls(AppClient.IOS, videoInfoQuery);
    }

    private VideoInfoHls getVideoInfoHls(AppClient client, String videoInfoQuery) {
        Call<VideoInfoHls> wrapper = mVideoInfoApi.getVideoInfoHls(videoInfoQuery, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper, !isAuthSupported(client) || mSkipAuthBlock);
    }

    private void applyFixesIfNeeded(VideoInfo result, String videoId, String clickTrackingParams) {
        if (result == null || result.isUnplayable()) {
            return;
        }

        if (result.isLive()) {
            Log.d(TAG, "Enable seeking support on live streams...");
            result.sync(getDashInfo(result));
        }

        if (shouldObtainExtendedFormats(result) || result.isStoryboardBroken()) {
            Log.d(TAG, "Enable high bitrate formats...");
            mSkipAuthBlock = true;
            VideoInfoHls videoInfoHls = getVideoInfoIOSHls(videoId, clickTrackingParams);
            mSkipAuthBlock = false;
            if (videoInfoHls != null && shouldObtainExtendedFormats(result)) {
                result.setHlsManifestUrl(videoInfoHls.getHlsManifestUrl());
            }
            if (videoInfoHls != null && result.isStoryboardBroken()) {
                result.setStoryboardSpec(videoInfoHls.getStoryboardSpec());
            }
        }

        // TV and others has a limited number of auto generated subtitles
        if (result.hasSubtitles() && shouldUnlockMoreSubtitles()) {
            Log.d(TAG, "Enable full list of auto generated subtitles...");

            if (mCachedTranslationLanguages == null) {
                mSkipAuthBlock = true;
                VideoInfo webInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
                mSkipAuthBlock = false;
                if (webInfo != null) {
                    mCachedTranslationLanguages = webInfo.getTranslationLanguages();
                }
            }

            if (mCachedTranslationLanguages != null) {
                result.setTranslationLanguages(mCachedTranslationLanguages);
            }
        }
    }

    private VideoInfo retryIfNeeded(VideoInfo videoInfo, String videoId, String clickTrackingParams) {
        if (videoInfo == null) {
            return null;
        }

        VideoInfo result = null;

        if (videoInfo.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfo(AppClient.TV, videoInfo.getTrailerVideoId(), clickTrackingParams);
        } else if (videoInfo.isUnplayable()) {
            result = getFirstPlayable(
                    () -> getVideoInfo(AppClient.TV, videoId, clickTrackingParams), // Supports Auth. Restricted (18+) videos
                    () -> getVideoInfoGeo(AppClient.WEB, videoId, clickTrackingParams) // Video clip blocked in current location
            );
        }

        return result != null ? result : videoInfo;
    }

    private VideoInfo getFirstPlayable(VideoInfoCallback... callbacks) {
        VideoInfo result = null;

        for (VideoInfoCallback callback : callbacks) {
            if (callback == null)
                continue;

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
        Pair<Integer, Boolean> videoInfoType = MediaServiceData.instance().getVideoInfoType();
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

    private static boolean shouldUnlockMoreSubtitles() {
        return MediaServiceData.instance().isMoreSubtitlesUnlocked();
    }

    private static boolean isAuthSupported(int videoInfoType) {
        // Only TV can work with auth
        return videoInfoType == VIDEO_INFO_TV;
    }

    private static boolean isAuthSupported(AppClient client) {
        // Only TV can work with auth
        return client == AppClient.TV;
    }

    private boolean isPotSupported(int videoInfoType) {
        return videoInfoType == VIDEO_INFO_WEB || videoInfoType == VIDEO_INFO_MWEB  || videoInfoType == WEB_EMBEDDED_PLAYER || videoInfoType == ANDROID_VR;
    }
}
