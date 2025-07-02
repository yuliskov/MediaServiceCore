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

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;

public class VideoInfoService extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoService.class.getSimpleName();
    private static VideoInfoService sInstance;
    private final VideoInfoApi mVideoInfoApi;
    // VIDEO_INFO_TV can bypass "Sign in to confirm you're not a bot" (rare case)
    // VIDEO_INFO_WEB_EMBED - the best one, with no occasional 403 errors
    // VIDEO_INFO_IOS can work without NSig.
    // VIDEO_INFO_TV and VIDEO_INFO_TV_EMBED are the only ones working in North America
    // VIDEO_INFO_MWEB - can bypass SABR-only responses
    private final static AppClient[] VIDEO_INFO_TYPE_LIST = {
            //VIDEO_INFO_TV, VIDEO_INFO_IOS, VIDEO_INFO_TV_EMBED, VIDEO_INFO_MWEB, VIDEO_INFO_ANDROID, VIDEO_INFO_INITIAL, VIDEO_INFO_WEB
            //VIDEO_INFO_WEB, VIDEO_INFO_MWEB, VIDEO_INFO_INITIAL, VIDEO_INFO_IOS, VIDEO_INFO_WEB_EMBED, VIDEO_INFO_ANDROID, VIDEO_INFO_TV, VIDEO_INFO_TV_EMBED
            AppClient.WEB_EMBED, AppClient.MWEB, AppClient.IOS, AppClient.TV, AppClient.TV_EMBED
    };
    @Nullable
    private AppClient mVideoInfoType = null;
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

        decipherFormats(result.getAdaptiveFormats());
        decipherFormats(result.getRegularFormats());

        if (result.isHistoryBroken()) {
            // Only the tv client supports auth features
            result.sync(getVideoInfo(AppClient.TV, videoId, clickTrackingParams));
        }

        return result;
    }

    private VideoInfo firstNonNull(String videoId, String clickTrackingParams) {
        final AppClient beginType = Arrays.asList(VIDEO_INFO_TYPE_LIST).contains(mVideoInfoType) ? mVideoInfoType : VIDEO_INFO_TYPE_LIST[0];
        AppClient nextType = beginType;
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

    private void initVideoInfo() {
        if (mVideoInfoType != null) {
            return;
        }

        resetData();
        restoreVideoInfoType();
    }

    public void switchNextFormat() {
        if (!mIsUnplayable && isPotSupported() && PoTokenGate.resetCache()) {
            return;
        }
        if (getData().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS)) {
            // Skip additional formats fetching that could produce an error
            getData().enableFormat(MediaServiceData.FORMATS_EXTENDED_HLS, false);
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
        mVideoInfoType = null;
        mSkipAuth = false;
        nextVideoInfo();
    }

    private void nextVideoInfo() {
        // The same format but without auth may behave better
        if (mVideoInfoType != null && !mSkipAuth) {
            mSkipAuth = true;
            return;
        }

        mVideoInfoType = Helpers.getNextValue(mVideoInfoType, VIDEO_INFO_TYPE_LIST);
        mSkipAuth = !isAuthSupported();
    }

    private VideoInfo getVideoInfo(AppClient client, String videoId, String clickTrackingParams) {
        if (client == AppClient.INITIAL) {
            return InitialResponse.getVideoInfo(videoId, mSkipAuthBlock);
        }

        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfoGeo(AppClient client, String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQueryGeo(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfo(AppClient client, String videoInfoQuery) {
        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorData(), client.getUserAgent());

        return getVideoInfo(wrapper, !client.isAuthSupported() || mSkipAuthBlock);
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

        return RetrofitHelper.get(wrapper, !client.isAuthSupported() || mSkipAuthBlock);
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
        if (needMoreSubtitles(result)) {
            Log.d(TAG, "Enable full list of auto generated subtitles...");

            if (mCachedTranslationLanguages == null || mCachedTranslationLanguages.size() < 100) {
                mSkipAuthBlock = true;
                VideoInfo webInfo = null;
                try {
                    webInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    () -> getVideoInfo(AppClient.WEB_EMBED, videoId, clickTrackingParams), // Restricted (18+) videos
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

        for (AppClient type : VIDEO_INFO_TYPE_LIST) {
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
        Pair<Integer, Boolean> videoInfoType = getData().getVideoInfoType();
        if (videoInfoType != null && videoInfoType.first != -1) {
            mVideoInfoType = videoInfoType.first < AppClient.values().length ? AppClient.values()[videoInfoType.first] : null;
            mSkipAuth = videoInfoType.second;
            if (!Arrays.asList(VIDEO_INFO_TYPE_LIST).contains(mVideoInfoType)) {
                mVideoInfoType = VIDEO_INFO_TYPE_LIST[0];
                getData().setVideoInfoType(mVideoInfoType != null ? mVideoInfoType.ordinal() : -1, mSkipAuth);
            }
        }
    }

    private void persistVideoInfoType() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        getData().setVideoInfoType(mVideoInfoType != null ? mVideoInfoType.ordinal() : -1, mSkipAuth);
    }

    private static boolean shouldObtainExtendedFormats(VideoInfo result) {
        return getData().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS) && result.isExtendedHlsFormatsBroken();
    }

    private static boolean shouldUnlockMoreSubtitles(VideoInfo videoInfo) {
        return videoInfo != null && videoInfo.hasSubtitles() && getData().isMoreSubtitlesUnlocked();
    }

    private static boolean needMoreSubtitles(VideoInfo videoInfo) {
        return videoInfo != null && videoInfo.hasSubtitles() && (videoInfo.getTranslationLanguages() == null || videoInfo.getTranslationLanguages().size() < 100);
    }

    @Override
    protected boolean isPotSupported() {
        return mVideoInfoType != null && mVideoInfoType.isPotSupported();
    }

    private boolean isAuthSupported() {
        return mVideoInfoType != null && mVideoInfoType.isAuthSupported();
    }

    private static MediaServiceData getData() {
        return MediaServiceData.instance();
    }
}
