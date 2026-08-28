package com.liskovsoft.youtubeapi.videoinfo.V2;

import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;
import com.liskovsoft.youtubeapi.innertube.initialresponse.InitialResponseService;
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
    private static final AppClient IOS_CLIENT = AppClient.VISIONOS;
    private static final AppClient TV_CLIENT = AppClient.TV_DOWNGRADED;
    private static final AppClient WEB_CLIENT = AppClient.WEB_EMBED;
    private static VideoInfoService sInstance;
    private final VideoInfoApi mVideoInfoApi;
    private final static AppClient[] VIDEO_INFO_TYPE_LIST = {
            AppClient.WEB_EMBED, // Restricted (18+) videos
            AppClient.VISIONOS, // no url formats
            AppClient.TV_DOWNGRADED, // probably unplayable (weird potoken format?)
            AppClient.TV, // Supports auth. Fixes "please sign in" bug! (the best for Premium users)
            //AppClient.ANDROID_REEL, // doesn't require pot and cipher (hangs on all engines)
            AppClient.WEB, // Fix video clip blocked in current location
            AppClient.WEB_SAFARI,
            AppClient.IOS,
            AppClient.GEO, // Fix video clip blocked in current location
            AppClient.MWEB, // single audio language
            AppClient.TV_LEGACY,
            AppClient.TV_EMBED, // single audio language
            AppClient.ANDROID_VR, // doesn't require pot and cipher (often hangs?)
            AppClient.TV_SIMPLY, // hangs?
            //AppClient.ANDROID_SDK_LESS, // doesn't require pot (hangs on Cronet!)
    };
    @Nullable
    private AppClient mActualInfoType = null;
    @Nullable
    private AppClient mNextInfoType = null;
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

        //initInfoTypeIfNeeded();
        //reorderTypeListIfNeeded();

        AppService.instance().resetClientPlaybackNonce(); // unique value per each video info

        VideoInfo result = firstPlayable(videoId, clickTrackingParams, true);

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

    private void reorderTypeListIfNeeded() {
        if (getData().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS)) {
            moveFirst(IOS_CLIENT);
        } else {
            moveFirst(WEB_CLIENT);
        }
    }

    private void moveFirst(AppClient client) {
        if (VIDEO_INFO_TYPE_LIST[0] != client) {
            Helpers.move(VIDEO_INFO_TYPE_LIST, Arrays.asList(VIDEO_INFO_TYPE_LIST).indexOf(client), 0);
        }
    }

    public VideoInfo getAuthVideoInfo(String videoId, String clickTrackingParams) {
        if (videoId == null) {
            return null;
        }

        // Only the tv client supports auth features
        return getVideoInfo(AppClient.TV, videoId, clickTrackingParams, true);
    }

    private VideoInfo firstPlayable(String videoId, String clickTrackingParams, boolean auth) {
        VideoInfo result = firstInfoWith(videoId, clickTrackingParams, info -> !info.isUnplayable(), auth);

        if (result == null) {
            result = firstInfoWith(videoId, clickTrackingParams, info -> info.getRegularFormats() != null, auth);
        }

        // No client can play it (e.g. members only content or the video is removed).
        // Return the unplayable response instead of null. Otherwise the caller gets an empty
        // result and reloads the video endlessly (infinite loading spinner) instead of
        // showing the real playability reason to the user.
        return result != null ? result : firstInfoWith(videoId, clickTrackingParams, info -> true, auth);
    }

    private interface InfoTester {
        boolean test(VideoInfo info);
    }

    private VideoInfo firstInfoWith(String videoId, String clickTrackingParams, InfoTester infoTester, boolean auth) {
        //final AppClient beginType = getDefaultClient();
        final AppClient beginType = mNextInfoType != null ? mNextInfoType : VIDEO_INFO_TYPE_LIST[0];
        AppClient nextType = beginType;

        do {
            VideoInfo result = getVideoInfoWithRentFix(nextType, videoId, clickTrackingParams, auth);

            if (result != null && infoTester.test(result)) {
                return result;
            }

            nextType = Helpers.getNextValue(VIDEO_INFO_TYPE_LIST, nextType);
        } while (nextType != beginType);

        return null;
    }

    //private void initInfoTypeIfNeeded() {
    //    if (mActualInfoType != null) {
    //        return;
    //    }
    //
    //    restoreVideoInfoType();
    //}

    public void switchNextFormat() {
        //initInfoTypeIfNeeded();

        // Try to reset pot cache for the last video
        if (!mIsUnplayable && mActualInfoType != null && PoTokenGate.resetCache(mActualInfoType)) {
            return;
        }
        // The Premium is likely broken
        //if (getData().isFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS)) {
        //    // Skip additional formats fetching that could produce an error
        //    getData().setFormatEnabled(MediaServiceData.FORMATS_EXTENDED_HLS, false);
        //    return;
        //}
        // And last, try to switch the client
        nextVideoInfoType();
        //persistVideoInfoType();
    }

    public void switchNextSubtitle() {
        CaptionTrack.sFormat = Helpers.getNextValue(CaptionTrack.CaptionFormat.values(), CaptionTrack.sFormat);
    }

    public void resetInfoType() {
        resetInfoTypeToDefault();
        PoTokenGate.resetCache();
    }

    private void nextVideoInfoType() {
        mNextInfoType = Helpers.getNextValue(VIDEO_INFO_TYPE_LIST, mActualInfoType);
    }

    private VideoInfo getVideoInfoWithRentFix(AppClient client, String videoId, String clickTrackingParams, boolean auth) {
        VideoInfo result = getVideoInfo(client, videoId, clickTrackingParams, auth);

        if (result != null && result.isRent()) {
            Log.e(TAG, "Found rent content. Show trailer instead...");
            result = getVideoInfo(client, result.getTrailerVideoId(), clickTrackingParams, auth);
        }

        return result;
    }

    private VideoInfo getVideoInfo(AppClient client, String videoId, String clickTrackingParams, boolean auth) {
        VideoInfo result;

        if (client == AppClient.INITIAL) {
            result = InitialResponseService.getVideoInfo(videoId, auth);
        } else {
            String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(client, videoId, clickTrackingParams);
            result = getVideoInfoQuery(client, videoInfoQuery, auth);
        }

        if (result != null) {
            result.setClient(client);
        }

        return result;
    }

    private VideoInfo getVideoInfoQuery(AppClient client, String videoInfoQuery, boolean authBlock) {
        boolean auth = client.isAuthSupported() && authBlock;

        if (client.isReelClient()) {
            Call<VideoInfoReel> wrapper = mVideoInfoApi.getVideoInfoReel(videoInfoQuery, mAppService.getVisitorData(),
                    client.getUserAgent(), client.getInnerTubeName(), client.getClientVersion());
            return getVideoInfoReel(wrapper, auth);
        }

        Call<VideoInfo> wrapper = mVideoInfoApi.getVideoInfo(videoInfoQuery, mAppService.getVisitorData(),
                client.getUserAgent(), client.getInnerTubeName(), client.getClientVersion());
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

    private VideoInfoHls getVideoInfoIOSHls(String videoId, String clickTrackingParams, boolean auth) {
        String videoInfoQuery = VideoInfoApiHelper.getVideoInfoQuery(IOS_CLIENT, videoId, clickTrackingParams);
        return getVideoInfoHls(IOS_CLIENT, videoInfoQuery, auth);
    }

    private VideoInfoHls getVideoInfoHls(AppClient client, String videoInfoQuery, boolean authBlock) {
        Call<VideoInfoHls> wrapper = mVideoInfoApi.getVideoInfoHls(videoInfoQuery, mAppService.getVisitorData(),
                client.getUserAgent(), client.getInnerTubeName(), client.getClientVersion());

        return RetrofitHelper.get(wrapper, client.isAuthSupported() && authBlock);
    }

    private void applyFixesIfNeeded(VideoInfo result, String videoId, String clickTrackingParams) {
        if (result == null || result.isUnplayable()) {
            return;
        }

        if (shouldObtainExtendedFormats(result) || result.isStoryboardBroken()) {
            Log.d(TAG, "Enable high bitrate formats...");
            VideoInfoHls videoInfoHls = getVideoInfoIOSHls(videoId, clickTrackingParams, false);
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
                VideoInfo webInfo = null;
                try {
                    webInfo = getVideoInfo(AppClient.WEB, videoId, clickTrackingParams, false);
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

    //private void restoreVideoInfoType() {
    //    int videoInfoType = getData().getVideoInfoType();
    //    if (videoInfoType != -1) {
    //        mActualInfoType = videoInfoType < AppClient.values().length ? AppClient.values()[videoInfoType] : null;
    //        if (!Arrays.asList(VIDEO_INFO_TYPE_LIST).contains(mActualInfoType)) {
    //            resetInfoTypeToDefault();
    //        }
    //    } else {
    //        resetInfoTypeToDefault();
    //    }
    //}

    private void resetInfoTypeToDefault() {
        mNextInfoType = null;
        mActualInfoType = VIDEO_INFO_TYPE_LIST[0];
        persistVideoInfoType();
    }

    private void persistVideoInfoType() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        getData().setVideoInfoType(mActualInfoType != null ? mActualInfoType.ordinal() : -1);
    }

    private void persistRecentTypeIfNeeded(VideoInfo videoInfo) {
        if (videoInfo == null || videoInfo.isUnplayable() || videoInfo.getClient() == mActualInfoType) {
            return;
        }

        mActualInfoType = videoInfo.getClient();
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

    private static boolean isAuthSupported(AppClient client) {
        return client != null && client.isAuthSupported();
    }
}
