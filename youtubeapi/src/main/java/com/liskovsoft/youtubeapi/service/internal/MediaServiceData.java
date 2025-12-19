package com.liskovsoft.youtubeapi.service.internal;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.app.models.cached.AppInfoCached;
import com.liskovsoft.youtubeapi.app.models.cached.ClientDataCached;
import com.liskovsoft.youtubeapi.app.models.cached.PlayerDataCached;
import com.liskovsoft.youtubeapi.app.playerdata.NSigData;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerExtractorCache;
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenResponse;
import com.liskovsoft.youtubeapi.service.YouTubeMediaItemService;

import java.util.UUID;

import io.reactivex.disposables.Disposable;
import kotlin.Triple;

public class MediaServiceData {
    private static final String TAG = MediaServiceData.class.getSimpleName();
    public static final int FORMATS_NONE = 0;
    public static final int FORMATS_ALL = Integer.MAX_VALUE;
    public static final int FORMATS_DASH = 1;
    public static final int FORMATS_URL = 1 << 1;
    public static final int FORMATS_EXTENDED_HLS = 1 << 2;
    public static final int CONTENT_NONE = 0;
    public static final int CONTENT_MIXES = 1;
    public static final int CONTENT_WATCHED_HOME = 1 << 1;
    public static final int CONTENT_WATCHED_SUBSCRIPTIONS = 1 << 2;
    public static final int CONTENT_SHORTS_HOME = 1 << 3;
    public static final int CONTENT_SHORTS_SEARCH = 1 << 4;
    public static final int CONTENT_WATCHED_WATCH_LATER = 1 << 5;
    public static final int CONTENT_SHORTS_SUBSCRIPTIONS = 1 << 6;
    public static final int CONTENT_SHORTS_HISTORY = 1 << 7;
    public static final int CONTENT_UPCOMING_CHANNEL = 1 << 8;
    public static final int CONTENT_UPCOMING_HOME = 1 << 9;
    public static final int CONTENT_SHORTS_TRENDING = 1 << 10;
    public static final int CONTENT_UPCOMING_SUBSCRIPTIONS = 1 << 11;
    public static final int CONTENT_STREAMS_SUBSCRIPTIONS = 1 << 12;
    public static final int CONTENT_SHORTS_CHANNEL = 1 << 13;
    private static MediaServiceData sInstance;
    private String mScreenId;
    private String mDeviceId;
    private String mOldAppVersion;
    private int mVideoInfoType;
    private String mVisitorCookie;
    private int mEnabledFormats;
    private int mHiddenContent;
    private Disposable mPersistAction;
    private MediaServiceCache mCachedPrefs;
    private GlobalPreferences mGlobalPrefs;
    private PoTokenResponse mPoToken;
    private AppInfoCached mAppInfo;
    private AppInfoCached mFailedAppInfo;
    private PlayerDataCached mPlayerData;
    private PlayerExtractorCache mPlayerExtractorCache;
    private ClientDataCached mClientData;
    private NSigData mNSigData;
    private NSigData mSigData;
    private boolean mIsMoreSubtitlesUnlocked;
    private boolean mIsLegacyUIEnabled;

    private static class MediaServiceCache extends SharedPreferencesBase {
        private static final String PREF_NAME = MediaServiceCache.class.getSimpleName();
        private static final String MEDIA_SERVICE_CACHE = "media_service_cache";

        public MediaServiceCache(Context context) {
            super(context, PREF_NAME, true);
        }

        public String getMediaServiceCache() {
            return getString(MEDIA_SERVICE_CACHE, null);
        }

        public void setMediaServiceCache(String cache) {
            putString(MEDIA_SERVICE_CACHE, cache);
        }
    }

    private MediaServiceData() {
        if (Helpers.isJUnitTest()) {
            Log.d(TAG, "JUnit test is running. Skipping data restore...");
            mEnabledFormats = FORMATS_ALL; // Debug
            mVideoInfoType = -1; // Required for testing
            return;
        }

        mGlobalPrefs = GlobalPreferences.sInstance;
        mCachedPrefs = new MediaServiceCache(mGlobalPrefs.getContext());

        restoreData();
        restoreCachedData();
    }

    public static MediaServiceData instance() {
        if (sInstance == null) {
            if (GlobalPreferences.sInstance == null && !Helpers.isJUnitTest()) {
                Log.e(TAG, "Can't init MediaServiceData. GlobalPreferences isn't initialized yet.");
                return null;
            }
            sInstance = new MediaServiceData();
        }

        return sInstance;
    }

    public String getScreenId() {
        return mScreenId;
    }

    public void setScreenId(String screenId) {
        mScreenId = screenId;
        persistData();
    }

    /**
     * Unique per app instance
     */
    public String getDeviceId() {
        if (mDeviceId == null) {
            mDeviceId = UUID.randomUUID().toString();
            persistData();
        }

        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
        persistData();
    }
    
    public int getVideoInfoType() {
        return mVideoInfoType;
    }

    public void setVideoInfoType(int videoInfoType) {
        mVideoInfoType = videoInfoType;
        persistData();
    }

    public Triple<NSigData, NSigData, PlayerDataCached> getPlayerExtractorData() {
        return new Triple<>(mNSigData, mSigData, mPlayerData);
    }

    public void setPlayerExtractorData(NSigData nSigData, NSigData sigData, PlayerDataCached playerData) {
        mNSigData = nSigData;
        mSigData = sigData;
        mPlayerData = playerData;
        persistData();
    }

    @Nullable
    public PlayerExtractorCache getPlayerExtractorCache() {
        return mPlayerExtractorCache;
    }

    public void setPlayerExtractorCache(PlayerExtractorCache playerCache) {
        mPlayerExtractorCache = playerCache;
        persistData();
    }

    public String getVisitorCookie() {
        return mVisitorCookie;
    }

    public void setVisitorCookie(String visitorCookie) {
        mVisitorCookie = visitorCookie;
        persistData();
    }

    public boolean isFormatEnabled(int formats) {
        if (mEnabledFormats == FORMATS_NONE) {
            setFormatEnabled(FORMATS_DASH | FORMATS_URL, true);
        }

        return (mEnabledFormats & formats) == formats;
    }

    public void setFormatEnabled(int formats, boolean enable) {
        if (enable) {
            mEnabledFormats |= formats;
        } else {
            mEnabledFormats &= ~formats;
        }

        persistData();

        YouTubeMediaItemService.instance().invalidateCache(); // Remove current cached video
    }

    public boolean isContentHidden(int content) {
        return (mHiddenContent & content) == content;
    }

    public void setContentHidden(int content, boolean hide) {
        if (hide) {
            mHiddenContent |= content;
        } else {
            mHiddenContent &= ~content;
        }

        persistData();
    }

    public PoTokenResponse getPoToken() {
        return mPoToken;
    }

    public void setPoToken(PoTokenResponse poToken) {
        mPoToken = poToken;

        persistData();
    }

    public AppInfoCached getAppInfo() {
        return mAppInfo;
    }

    public void setAppInfo(AppInfoCached appInfo) {
        if (appInfo != null) {
            mFailedAppInfo = null;
        }

        if (Helpers.equals(mAppInfo, appInfo)) {
            return;
        }

        mAppInfo = appInfo;

        persistData();
    }

    public AppInfoCached getFailedAppInfo() {
        return mFailedAppInfo;
    }

    public void setFailedAppInfo(AppInfoCached appInfo) {
        if (Helpers.equals(mFailedAppInfo, appInfo)) {
            return;
        }

        mFailedAppInfo = appInfo;

        persistData();
    }

    public ClientDataCached getClientData() {
        return mClientData;
    }

    public void setClientData(ClientDataCached clientData) {
        mClientData = clientData;

        persistData();
    }

    public boolean isMoreSubtitlesUnlocked() {
        return mIsMoreSubtitlesUnlocked;
    }

    public void setMoreSubtitlesUnlocked(boolean unlock) {
        mIsMoreSubtitlesUnlocked = unlock;
        persistData();

        YouTubeMediaItemService.instance().invalidateCache(); // Remove current cached video
    }

    public boolean isPotSupported() {
        return PoTokenGate.isWebPotSupported();
    }

    public boolean isLegacyUIEnabled() {
        return mIsLegacyUIEnabled;
    }

    public void setLegacyUIEnabled(boolean enable) {
        mIsLegacyUIEnabled = enable;

        persistData();
    }

    private void restoreData() {
        String data = mGlobalPrefs.getMediaServiceData();

        String[] split = Helpers.splitData(data);

        String appVersion = AppInfoHelpers.getAppVersionName(mGlobalPrefs.getContext());

        // null for ScreenItem
        mScreenId = Helpers.parseStr(split, 1);
        mDeviceId = Helpers.parseStr(split, 2);
        //String lastPlayerUrl = AppConstants.playerUrls.get(0); // fallback url for nfunc extractor
        mOldAppVersion = Helpers.parseStr(split, 3);
        mVideoInfoType = Helpers.parseInt(split, 4, -1);
        //mSkipAuth = Helpers.parseBoolean(split, 5);
        // entries here moved to the cache
        mEnabledFormats = Helpers.parseInt(split, 11, FORMATS_DASH | FORMATS_URL);
        // null
        mPoToken = Helpers.parseItem(split, 14, PoTokenResponse::fromString);
        mAppInfo = Helpers.parseItem(split, 15, AppInfoCached::fromString);
        mPlayerData = Helpers.parseItem(split, 16, PlayerDataCached::fromString);
        mClientData = Helpers.parseItem(split, 17, ClientDataCached::fromString);
        mHiddenContent = Helpers.parseInt(split, 18,
                CONTENT_SHORTS_SUBSCRIPTIONS | CONTENT_SHORTS_HISTORY | CONTENT_SHORTS_TRENDING | CONTENT_UPCOMING_CHANNEL | CONTENT_UPCOMING_HOME | CONTENT_UPCOMING_SUBSCRIPTIONS);
        mIsMoreSubtitlesUnlocked = Helpers.parseBoolean(split, 19);
        //mIsPremiumFixEnabled = Helpers.parseBoolean(split, 20);
        mVisitorCookie = Helpers.parseStr(split, 21);
        mIsLegacyUIEnabled = Helpers.parseBoolean(split, 23);
        mFailedAppInfo = Helpers.parseItem(split, 24, AppInfoCached::fromString);

        boolean isAppUpdated = mOldAppVersion != null && !Helpers.equals(mOldAppVersion, appVersion);

        if (isAppUpdated) {
            resetSensitiveData();
        }

        mOldAppVersion = appVersion;
    }

    private void restoreCachedData() {
        String cache = mCachedPrefs.getMediaServiceCache();

        String[] split = Helpers.splitData(cache);

        mNSigData = Helpers.parseItem(split, 8, NSigData::fromString);
        mSigData = Helpers.parseItem(split, 9, NSigData::fromString);
        //mPlayerExtractorVersion = Helpers.parseStr(split, 10);
        mPlayerExtractorCache = Helpers.parseItem(split, 11, PlayerExtractorCache::fromString);
    }

    private void persistDataInt() {
        if (mGlobalPrefs == null) {
            return;
        }

        mGlobalPrefs.setMediaServiceData(
                Helpers.mergeData(null, mScreenId, mDeviceId, mOldAppVersion,
                        mVideoInfoType, null, null, null, null, null,
                        null, mEnabledFormats, null, null, mPoToken, mAppInfo,
                        mPlayerData, mClientData, mHiddenContent, mIsMoreSubtitlesUnlocked,
                        null, mVisitorCookie, null, mIsLegacyUIEnabled, mFailedAppInfo));
    }

    private void persistCachedDataInt() {
        if (mCachedPrefs == null) {
            return;
        }

        mCachedPrefs.setMediaServiceCache(
                Helpers.mergeData(null, null, null, null, null, null,
                        null, null, mNSigData, mSigData, null, mPlayerExtractorCache));
    }

    public void persistNow() {
        RxHelper.disposeActions(mPersistAction);

        mPersistAction = RxHelper.runAsync(() -> { persistDataInt(); persistCachedDataInt(); });
    }

    private void persistData() {
        RxHelper.disposeActions(mPersistAction);

        // Improve memory usage by merging multiple persist requests
        mPersistAction = RxHelper.runAsync(() -> { persistDataInt(); persistCachedDataInt(); }, 5_000);
    }

    private void resetSensitiveData() {
        mVideoInfoType = -1;
        mFailedAppInfo = null;
    }
}
