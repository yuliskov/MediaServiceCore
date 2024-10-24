package com.liskovsoft.youtubeapi.service.internal;

import android.content.Context;
import android.util.Pair;

import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.sharedutils.prefs.SharedPreferencesBase;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.app.AppConstants;

import java.util.List;
import java.util.UUID;

import io.reactivex.disposables.Disposable;

public class MediaServiceData {
    private static final String TAG = MediaServiceData.class.getSimpleName();
    public static final int FORMATS_ALL = Integer.MAX_VALUE;
    public static final int FORMATS_DASH = 1;
    public static final int FORMATS_URL = 1 << 1;
    public static final int FORMATS_EXTENDED_HLS = 1 << 2;
    public static final int CONTENT_ALL = Integer.MAX_VALUE;
    public static final int CONTENT_MIXES = 1;
    public static final int CONTENT_WATCHED_HOME = 1 << 1;
    public static final int CONTENT_WATCHED_SUBS = 1 << 2;
    public static final int CONTENT_SHORTS_HOME = 1 << 3;
    private static MediaServiceData sInstance;
    private String mAppVersion;
    private String mScreenId;
    private String mDeviceId;
    private String mVideoInfoVersion;
    private int mVideoInfoType;
    private String mNFuncPlayerUrl;
    private List<String> mNFuncParams;
    private String mNFuncCode;
    private String mPlayerUrl;
    private String mPlayerVersion;
    private String mVisitorCookie;
    private int mEnabledFormats = FORMATS_ALL;
    private int mEnabledContent = CONTENT_ALL;
    private Disposable mPersistAction;
    private boolean mSkipAuth;
    private MediaServiceCache mCachedPrefs;
    private GlobalPreferences mGlobalPrefs;

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

    public String getPlayerUrl() {
        if (Helpers.equals(mPlayerVersion, mAppVersion)) {
            return mPlayerUrl;
        }

        return null;
    }

    public void setPlayerUrl(String url) {
        mPlayerVersion = mAppVersion;
        mPlayerUrl = url;
        persistData();
    }

    public String getNFuncPlayerUrl() {
        return mNFuncPlayerUrl;
    }

    public void setNFuncPlayerUrl(String playerUrl) {
        mNFuncPlayerUrl = playerUrl;
        persistData();
    }

    public List<String> getNFuncParams() {
        return mNFuncParams;
    }

    public void setNFuncParams(List<String> nFuncParams) {
        mNFuncParams = nFuncParams;
        persistData();
    }
    
    public String getNFuncCode() {
        return mNFuncCode;
    }

    public void setNFuncCode(String nFuncCode) {
        mNFuncCode = nFuncCode;
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
        return (mEnabledFormats & formats) == formats;
    }

    public void enableContent(int content, boolean enable) {
        if (enable) {
            mEnabledContent |= content;
        } else {
            mEnabledContent &= ~content;
        }

        persistData();
    }

    public boolean isContentEnabled(int content) {
        return (mEnabledContent & content) == content;
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
        mEnabledContent = Helpers.parseInt(split, 12, CONTENT_MIXES);
        mSkipAuth = Helpers.parseBoolean(split, 13);
    }

    private void restoreCachedData() {
        String cache = mCachedPrefs.getMediaServiceCache();

        String[] split = Helpers.splitData(cache);

        mAppVersion = AppInfoHelpers.getAppVersionName(mGlobalPrefs.getContext());

        String lastPlayerUrl = AppConstants.playerUrls.get(AppConstants.playerUrls.size() - 1); // fallback url for nfunc extractor
        mVideoInfoVersion = Helpers.parseStr(split, 0);
        mVideoInfoType = Helpers.parseInt(split, 1);
        mNFuncPlayerUrl = Helpers.parseStr(split, 2, lastPlayerUrl);
        mNFuncParams = Helpers.parseStrList(split, 3);
        mNFuncCode = Helpers.parseStr(split, 4);
        mPlayerUrl = Helpers.parseStr(split, 5);
        mPlayerVersion = Helpers.parseStr(split, 6);
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
                        mVisitorCookie, mEnabledFormats, mEnabledContent, mSkipAuth));
    }

    private void persistCachedDataReal() {
        if (mCachedPrefs == null) {
            return;
        }

        mCachedPrefs.setMediaServiceCache(
                Helpers.mergeData(mVideoInfoVersion, mVideoInfoType,
                        mNFuncPlayerUrl, mNFuncParams, mNFuncCode, mPlayerUrl, mPlayerVersion));
    }
}
