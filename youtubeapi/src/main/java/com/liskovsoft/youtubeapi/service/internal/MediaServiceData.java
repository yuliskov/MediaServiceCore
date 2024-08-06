package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.helpers.AppInfoHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;

import java.util.List;
import java.util.UUID;

public class MediaServiceData {
    private static final String TAG = MediaServiceData.class.getSimpleName();
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

    /**
     * Only for testing purposes
     */
    public void resetAll() {
        if (GlobalPreferences.sInstance != null) {
            GlobalPreferences.sInstance.setMediaServiceData(null);
        }
    }

    private void restoreData() {
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "Can't restore data. GlobalPreferences isn't initialized yet.");
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
        mNFuncPlayerUrl = Helpers.parseStr(split, 5);
        mNFuncParams = Helpers.parseStrList(split, 6);
        mNFuncCode = Helpers.parseStr(split, 7);
        mBackupPlayerUrl = Helpers.parseStr(split, 8);
        mBackupPlayerVersion = Helpers.parseStr(split, 9);
        mVisitorCookie = Helpers.parseStr(split, 10);
    }

    private void persistData() {
        if (GlobalPreferences.sInstance == null) {
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceData(
                Helpers.mergeData(null, mScreenId, mDeviceId, mVideoInfoVersion, mVideoInfoType,
                        mNFuncPlayerUrl, mNFuncParams, mNFuncCode, mBackupPlayerUrl, mBackupPlayerVersion, mVisitorCookie));
    }
}
