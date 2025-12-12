package com.liskovsoft.youtubeapi.videoinfo.V2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;
import com.liskovsoft.youtubeapi.videoinfo.InitialResponse;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.CaptionTrack;
import com.liskovsoft.youtubeapi.videoinfo.models.TranslationLanguage;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoHls;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoReel;

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
            AppClient.WEB_EMBED, // Restricted (18+) videos
            //AppClient.ANDROID_SDK_LESS, // doesn't require pot (hangs on cronet!)
            AppClient.ANDROID_REEL, // doesn't require pot and cipher
            AppClient.IOS,
            AppClient.TV, // Supports auth. Fixes "please sign in" bug!
            AppClient.TV_LEGACY,
            AppClient.TV_DOWNGRADED,
            AppClient.TV_EMBED, // single audio language
            AppClient.TV_SIMPLY,
            AppClient.GEO, // Fix video clip blocked in current location
            AppClient.MWEB, // single audio language
            AppClient.WEB_SAFARI,
            AppClient.WEB, // Fix video clip blocked in current location
    };
    @Nullable
    private AppClient mVideoInfoType = null;
    @Nullable
    private AppClient mRecentInfoType = null;
    private boolean mAuthBlock;
    private List<TranslationLanguage> mCachedTranslationLanguages;
    private boolean mIsUnplayable;

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
        if (videoId == null) {
            return null;
        }

        initInfoTypeIfNeeded();

        AppService.instance().resetClientPlaybackNonce(); // unique value per each video info

        mAuthBlock = true;

        VideoInfo result = firstPlayable(videoId, clickTrackingParams);

        if (result == null) {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
            return null;
        }

        applyFixesIfNeeded(result, videoId, clickTrackingParams);

        transformFormats(result);

        persistRecentTypeIfNeeded(result);

        mIsUnplayable = result.isUnplayable();

        return result;
    }

    public VideoInfo getAuthVideoInfo(String videoId, String clickTrackingParams) {
        if (videoId == null) {
            return null;
        }

        mAuthBlock = true;

        // Only the tv client supports auth features
        return getVideoInfo(AppClient.TV, videoId, clickTrackingParams);
    }

    private VideoInfo firstPlayable(String videoId, String clickTrackingParams) {
        VideoInfo result = firstInfoWith(videoId, clickTrackingParams, info -> !info.isUnplayable());

        return result != null ? result : firstInfoWith(videoId, clickTrackingParams, info -> true);
    }

    private interface InfoTester {
        boolean test(VideoInfo info);
    }

    private VideoInfo firstInfoWith(String videoId, String clickTrackingParams, InfoTester infoTester) {
        final AppClient beginType = getDefaultClient();
        AppClient nextType = beginType;

        do {
            VideoInfo result = getVideoInfoWithRentFix(nextType, videoId, clickTrackingParams);

            if (result != null && infoTester.test(result)) {
                return result;
            }

            nextType = Helpers.getNextValue(VIDEO_INFO_TYPE_LIST, nextType);
        } while (nextType != beginType);

        return null;
    }

    private void initInfoTypeIfNeeded() {
        if (mVideoInfoType != null) {
            return;
        }
        
        restoreVideoInfoType();
    }

    public void switchNextFormat() {
        initInfoTypeIfNeeded();

        // Try to reset pot cache for the last video
        if (!mIsUnplayable && PoTokenGate.resetCache(getClient())) {
            return;
        }
        // The Premium is likely broken
        if (getData().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS)) {
            // Skip additional formats fetching that could produce an error
            getData().setFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS, false);
            return;
        }
        // And last, try to switch the client
        nextVideoInfoType();
        persistVideoInfoType();
    }

    public void switchNextSubtitle() {
        CaptionTrack.sFormat = Helpers.getNextValue(CaptionTrack.CaptionFormat.values(), CaptionTrack.sFormat);
    }

    public void resetInfoType() {
        mVideoInfoType = null;
        persistVideoInfoType();
        PoTokenGate.resetCache(getClient());
    }

    private void nextVideoInfoType() {
        mVideoInfoType = Helpers.getNextValue(VIDEO_INFO_TYPE_LIST, mVideoInfoType);
    }

    private VideoInfo getVideoInfoWithRentFix(AppClient client, String videoId, String clickTrackingParams) {
        VideoInfo result = getVideoInfo(client, videoId, clickTrackingParams);

        if (result != null && result.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfo(client, result.getTrailerVideoId(), clickTrackingParams);
        }

        return result;
    }

    private VideoInfo getVideoInfo(AppClient client, String videoId, String clickTrackingParams) {
        if (client.isPlaybackBroken()) {
            return null;
        }

        mRecentInfoType = client;

        if (client == AppClient.INITIAL) {
            return InitialResponse.getVideoInfo(videoId, mAuthBlock);
        }

        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(client, videoId, clickTrackingParams);
        return getVideoInfo(client, videoInfoQuery);
    }

    private VideoInfo getVideoInfo(AppClient client, String videoInfoQuery) {
        boolean auth = client.isAuthSupported() && mAuthBlock;

        if (client.isReelClient()) {
            Call<VideoInfoReel> wrapper = mVideoInfoApi.getVideoInfoReel(videoInfoQuery, mAppService.getVisitorData(), client.getUserAgent());
            return getVideoInfoReel(wrapper, auth);
        }

        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorData(), client.getUserAgent());
        return getVideoInfo(wrapper, auth);
    }

    private @Nullable VideoInfo getVideoInfo(Call<VideoInfo> wrapper, boolean auth) {
        VideoInfo videoInfo = RetrofitHelper.get(wrapper, auth);

        if (videoInfo == null) {
            return null;
        }

        videoInfo.setAuth(auth);

        return videoInfo;
    }

    private @Nullable VideoInfo getVideoInfoReel(Call<VideoInfoReel> wrapper, boolean auth) {
        VideoInfoReel videoInfo = RetrofitHelper.get(wrapper, auth);

        if (videoInfo == null || videoInfo.getVideoInfo() == null) {
            return null;
        }

        videoInfo.getVideoInfo().setAuth(auth);

        return videoInfo.getVideoInfo();
    }

    private VideoInfoHls getVideoInfoIOSHls(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(AppClient.IOS, videoId, clickTrackingParams);
        return getVideoInfoHls(AppClient.IOS, videoInfoQuery);
    }

    private VideoInfoHls getVideoInfoHls(AppClient client, String videoInfoQuery) {
        Call<VideoInfoHls> wrapper = mVideoInfoApi.getVideoInfoHls(videoInfoQuery, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper, client.isAuthSupported() && mAuthBlock);
    }

    private void applyFixesIfNeeded(VideoInfo result, String videoId, String clickTrackingParams) {
        if (result == null || result.isUnplayable()) {
            return;
        }

        if (shouldObtainExtendedFormats(result) || result.isStoryboardBroken()) {
            Log.d(TAG, "Enable high bitrate formats...");
            mAuthBlock = false;
            VideoInfoHls videoInfoHls = getVideoInfoIOSHls(videoId, clickTrackingParams);
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
                mAuthBlock = false;
                VideoInfo webInfo = null;
                try {
                    webInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (webInfo != null) {
                    mCachedTranslationLanguages = webInfo.getTranslationLanguages();
                }
            }

            if (mCachedTranslationLanguages != null) {
                result.setTranslationLanguages(mCachedTranslationLanguages);
            }
        }
    }

    private void restoreVideoInfoType() {
        int videoInfoType = getData().getVideoInfoType();
        if (videoInfoType != -1) {
            mVideoInfoType = videoInfoType < AppClient.values().length ? AppClient.values()[videoInfoType] : null;
            if (!Arrays.asList(VIDEO_INFO_TYPE_LIST).contains(mVideoInfoType)) {
                mVideoInfoType = VIDEO_INFO_TYPE_LIST[0];
                getData().setVideoInfoType(mVideoInfoType != null ? mVideoInfoType.ordinal() : -1);
            }
        } else {
            mVideoInfoType = VIDEO_INFO_TYPE_LIST[0];
        }
    }

    private void persistVideoInfoType() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        getData().setVideoInfoType(mVideoInfoType != null ? mVideoInfoType.ordinal() : -1);
    }

    private void persistRecentTypeIfNeeded(VideoInfo videoInfo) {
        if (videoInfo == null || videoInfo.isUnplayable() || mRecentInfoType == null || mRecentInfoType == mVideoInfoType) {
            return;
        }

        mVideoInfoType = mRecentInfoType;
        persistVideoInfoType();
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
    protected AppClient getClient() {
        return mRecentInfoType != null ? mRecentInfoType : getDefaultClient();
    }

    private static boolean isAuthSupported(AppClient client) {
        return client != null && client.isAuthSupported();
    }

    private static MediaServiceData getData() {
        return MediaServiceData.instance();
    }

    @NonNull
    private AppClient getDefaultClient() {
        return mVideoInfoType != null && Arrays.asList(VIDEO_INFO_TYPE_LIST).contains(mVideoInfoType) ? mVideoInfoType : VIDEO_INFO_TYPE_LIST[0];
    }
}
