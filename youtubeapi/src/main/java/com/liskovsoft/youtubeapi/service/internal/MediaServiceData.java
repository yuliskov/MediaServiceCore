package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;

import java.util.UUID;

public class MediaServiceData {
    private static final String TAG = MediaServiceData.class.getSimpleName();
    private static MediaServiceData sInstance;
    private String mScreenId;
    private String mDeviceId;
    private String mVideoInfoVersion;
    private int mVideoInfoType;

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

    public String getVideoInfoVersion() {
        return mVideoInfoVersion;
    }

    public void setVideoInfoVersion(String videoInfoVersion) {
        mVideoInfoVersion = videoInfoVersion;
        persistData();
    }

    public int getVideoInfoType() {
        return mVideoInfoType;
    }

    public void setVideoInfoType(int videoInfoType) {
        mVideoInfoType = videoInfoType;
        persistData();
    }

    private void restoreData() {
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "Can't restore data. GlobalPreferences isn't initialized yet.");
            return;
        }

        String data = GlobalPreferences.sInstance.getMediaServiceData();

        String[] split = Helpers.splitData(data);

        mScreenId = Helpers.parseStr(split, 1);
        mDeviceId = Helpers.parseStr(split, 2);
        mVideoInfoVersion = Helpers.parseStr(split, 3);
        mVideoInfoType = Helpers.parseInt(split, 4);
    }

    private void persistData() {
        if (GlobalPreferences.sInstance == null) {
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceData(
                Helpers.mergeData(null, mScreenId, mDeviceId, mVideoInfoVersion, mVideoInfoType)); // null for ScreenItem
    }
}
