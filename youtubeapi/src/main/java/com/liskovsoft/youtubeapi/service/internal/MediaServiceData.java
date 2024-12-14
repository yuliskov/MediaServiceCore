package com.liskovsoft.youtubeapi.service.internal;

import android.content.Context;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.models.cached.AppInfoCached;
import com.liskovsoft.youtubeapi.app.models.cached.ClientDataCached;
import com.liskovsoft.youtubeapi.app.models.cached.PlayerDataCached;
import com.liskovsoft.youtubeapi.app.nsig.NSigData;
import com.liskovsoft.youtubeapi.app.potokencloud.PoTokenResponse;

import java.util.List;
import java.util.UUID;

import io.reactivex.disposables.Disposable;

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
    public static final int CONTENT_WATCHED_SUBS = 1 << 2;
    public static final int CONTENT_SHORTS_HOME = 1 << 3;
    public static final int CONTENT_SHORTS_SEARCH = 1 << 4;
    public static final int CONTENT_WATCHED_WATCH_LATER = 1 << 4;
    private static MediaServiceData sInstance;
    private String mAppVersion;
    private String mScreenId;
    private String mDeviceId;
    private String mVideoInfoVersion;
    private int mVideoInfoType;
    private String mPlayerUrl;
    private String mPlayerVersion;
    private String mVisitorCookie;
    private int mEnabledFormats = FORMATS_ALL; // Debug
    private int mHiddenContent;
    private Disposable mPersistAction;
    private boolean mSkipAuth;
    private MediaServiceCache mCachedPrefs;
    private GlobalPreferences mGlobalPrefs;
    private PoTokenResponse mPoToken;
    private AppInfoCached mAppInfo;
    private PlayerDataCached mPlayerData;
    private ClientDataCached mClientData;
    private NSigData mNSigData;

    private static class MediaServiceCache extends SharedPreferencesBase {
        private static final String PREF_NAME = MediaServiceCache.class.getSimpleName();
        private static final String MEDIA_SERVICE_CACHE = "media_service_cache";

        public MediaServiceCache(Context context) {
            super(context, PREF_NAME);
        }

        public String getMediaServiceCache() {
            return getString(MEDIA_SERVICE_CACHE, null);
        }

        public void setMediaServiceCache(String cache) {
            putString(MEDIA_SERVICE_CACHE, cache);
        }
    }

    private MediaServiceData() {
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "Can't restore data. GlobalPreferences isn't initialized yet.");
            return;
        }

        if (Helpers.isJUnitTest()) {
            Log.d(TAG, "JUnit test is running. Skipping data restore...");
            return;
        }

        mGlobalPrefs = GlobalPreferences.sInstance;
        mCachedPrefs = new MediaServiceCache(mGlobalPrefs.getContext());

        restoreData();
        restoreCachedData();
    }

    public static MediaServiceData instance() {
        if (sInstance == null) {
            sInstance = new MediaServiceData();
        }

        return sInstance;
    }

    public void setScreenId(String screenId) {
        mScreenId = screenId;
        persistData();
    }

    public String getScreenId() {
        return mScreenId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
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

    public Pair<Integer, Boolean> getVideoInfoType() {
        if (Helpers.equals(mVideoInfoVersion, mAppVersion)) {
            return new Pair<>(mVideoInfoType, mSkipAuth);
        }

        return null;
    }

    public void setVideoInfoType(int videoInfoType, boolean skipAuth) {
        mVideoInfoVersion = mAppVersion;
        mVideoInfoType = videoInfoType;
        mSkipAuth = skipAuth;
        persistData();
    }

    //public String getPlayerUrl() {
    //    if (Helpers.equals(mPlayerVersion, mAppVersion)) {
    //        return mPlayerUrl;
    //    }
    //
    //    return null;
    //}
    //
    //public void setPlayerUrl(String url) {
    //    mPlayerVersion = mAppVersion;
    //    mPlayerUrl = url;
    //    persistData();
    //}

    @Nullable
    public NSigData getNSigData() {
        return mNSigData;
    }

    public void setNSigData(NSigData nSigData) {
        mNSigData = nSigData;
        persistData();
    }

    public String getVisitorCookie() {
        return mVisitorCookie;
    }

    public void setVisitorCookie(String visitorCookie) {
        mVisitorCookie = visitorCookie;
        persistData();
    }

    public void enableFormat(int formats, boolean enable) {
        if (enable) {
            mEnabledFormats |= formats;
        } else {
            mEnabledFormats &= ~formats;
        }

        persistData();
    }

    public boolean isFormatEnabled(int formats) {
        if (mEnabledFormats == FORMATS_NONE) {
            enableFormat(FORMATS_DASH, true);
        }

        return (mEnabledFormats & formats) == formats;
    }

    public void hideContent(int content, boolean hide) {
        if (hide) {
            mHiddenContent |= content;
        } else {
            mHiddenContent &= ~content;
        }

        persistData();
    }

    public boolean isContentHidden(int content) {
        return (mHiddenContent & content) == content;
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
        mAppInfo = appInfo;

        persistData();
    }

    public PlayerDataCached getPlayerData() {
        return mPlayerData;
    }

    public void setPlayerData(PlayerDataCached playerData) {
        mPlayerData = playerData;

        persistData();
    }

    public ClientDataCached getClientData() {
        return mClientData;
    }

    public void setClientData(ClientDataCached clientData) {
        mClientData = clientData;

        persistData();
    }

    private void restoreData() {
        String data = mGlobalPrefs.getMediaServiceData();

        String[] split = Helpers.splitData(data);

        mAppVersion = AppInfoHelpers.getAppVersionName(mGlobalPrefs.getContext());

        // null for ScreenItem
        mScreenId = Helpers.parseStr(split, 1);
        mDeviceId = Helpers.parseStr(split, 2);
        // entries here moved to the cache
        mVisitorCookie = Helpers.parseStr(split, 10);
        mEnabledFormats = Helpers.parseInt(split, 11, FORMATS_DASH);
        mHiddenContent = Helpers.parseInt(split, 12, CONTENT_NONE);
        mSkipAuth = Helpers.parseBoolean(split, 13);
        mPoToken = Helpers.parseItem(split, 14, PoTokenResponse::fromString);
        mAppInfo = Helpers.parseItem(split, 15, AppInfoCached::fromString);
        mPlayerData = Helpers.parseItem(split, 16, PlayerDataCached::fromString);
        mClientData = Helpers.parseItem(split, 17, ClientDataCached::fromString);
    }

    private void restoreCachedData() {
        String cache = mCachedPrefs.getMediaServiceCache();

        String[] split = Helpers.splitData(cache);

        mAppVersion = AppInfoHelpers.getAppVersionName(mGlobalPrefs.getContext());

        String lastPlayerUrl = AppConstants.playerUrls.get(0); // fallback url for nfunc extractor
        mVideoInfoVersion = Helpers.parseStr(split, 0);
        mVideoInfoType = Helpers.parseInt(split, 1);
        //mNFuncPlayerUrl = Helpers.parseStr(split, 2, lastPlayerUrl);
        //mNFuncParams = Helpers.parseStrList(split, 3);
        //mNFuncCode = Helpers.parseStr(split, 4);
        mPlayerUrl = Helpers.parseStr(split, 5);
        mPlayerVersion = Helpers.parseStr(split, 6);
        mNSigData = Helpers.parseItem(split, 7, NSigData::fromString);
    }

    private void persistData() {
        RxHelper.disposeActions(mPersistAction);

        // Improve memory usage by merging multiple persist requests
        mPersistAction = RxHelper.runAsync(() -> { persistDataReal(); persistCachedDataReal(); }, 5_000);
    }

    private void persistDataReal() {
        if (mGlobalPrefs == null) {
            return;
        }

        mGlobalPrefs.setMediaServiceData(
                Helpers.mergeData(null, mScreenId, mDeviceId, null, null,
                        null, null, null, null, null,
                        mVisitorCookie, mEnabledFormats, mHiddenContent, mSkipAuth, mPoToken, mAppInfo, mPlayerData, mClientData));
    }

    private void persistCachedDataReal() {
        if (mCachedPrefs == null) {
            return;
        }

        mCachedPrefs.setMediaServiceCache(
                Helpers.mergeData(mVideoInfoVersion, mVideoInfoType,
                        null, null, null, mPlayerUrl, mPlayerVersion, mNSigData));
    }
}
