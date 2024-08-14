package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
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
    private String mBackupPlayerUrl;
    private String mBackupPlayerVersion;
    private String mVisitorCookie;
    private int mEnabledFormats = FORMATS_ALL;
    private int mEnabledContent = CONTENT_ALL;
    private Disposable mPersistAction;

    private MediaServiceData() {
        restoreData();
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

    public int getVideoInfoType() {
        if (Helpers.equals(mVideoInfoVersion, mAppVersion)) {
            return mVideoInfoType;
        }

        return -1;
    }

    public void setVideoInfoType(int videoInfoType) {
        mVideoInfoVersion = mAppVersion;
        mVideoInfoType = videoInfoType;
        persistData();
    }

    public String getBackupPlayerUrl() {
        if (Helpers.equals(mBackupPlayerVersion, mAppVersion)) {
            return mBackupPlayerUrl;
        }

        return null;
    }

    public void setBackupPlayerUrl(String url) {
        mBackupPlayerVersion = mAppVersion;
        mBackupPlayerUrl = url;
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
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "Can't restore data. GlobalPreferences isn't initialized yet.");
            return;
        }

        if (Helpers.isJUnitTest()) {
            Log.d(TAG, "JUnit test is running. Skipping data restore...");
            return;
        }

        String data = GlobalPreferences.sInstance.getMediaServiceData();

        String[] split = Helpers.splitData(data);

        mAppVersion = AppInfoHelpers.getAppVersionName(GlobalPreferences.sInstance.getContext());

        // null for ScreenItem
        mScreenId = Helpers.parseStr(split, 1);
        mDeviceId = Helpers.parseStr(split, 2);
        mVideoInfoVersion = Helpers.parseStr(split, 3);
        mVideoInfoType = Helpers.parseInt(split, 4);
        String lastPlayerUrl = AppConstants.playerUrls.get(AppConstants.playerUrls.size() - 1); // fallback url for nfunc extractor
        mNFuncPlayerUrl = Helpers.parseStr(split, 5, lastPlayerUrl);
        mNFuncParams = Helpers.parseStrList(split, 6);
        mNFuncCode = Helpers.parseStr(split, 7);
        mBackupPlayerUrl = Helpers.parseStr(split, 8);
        mBackupPlayerVersion = Helpers.parseStr(split, 9);
        mVisitorCookie = Helpers.parseStr(split, 10);
        mEnabledFormats = Helpers.parseInt(split, 11, FORMATS_DASH);
        mEnabledContent = Helpers.parseInt(split, 12, CONTENT_MIXES);
    }

    private void persistData() {
        RxHelper.disposeActions(mPersistAction);

        // Improve memory usage by merging multiple persist requests
        mPersistAction = RxHelper.runAsync(this::persistDataReal, 5_000);
    }

    private void persistDataReal() {
        if (GlobalPreferences.sInstance == null) {
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceData(
                Helpers.mergeData(null, mScreenId, mDeviceId, mVideoInfoVersion, mVideoInfoType,
                        mNFuncPlayerUrl, mNFuncParams, mNFuncCode, mBackupPlayerUrl, mBackupPlayerVersion,
                        mVisitorCookie, mEnabledFormats, mEnabledContent));
    }
}
