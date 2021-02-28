package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;

public class MediaServiceData {
    private static final String TAG = MediaServiceData.class.getSimpleName();
    private static MediaServiceData sInstance;
    private String mScreenId;

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

    private void restoreData() {
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "Can't restore data. GlobalPreferences isn't initialized yet.");
            return;
        }

        String data = GlobalPreferences.sInstance.getMediaServiceData();

        String[] split = Helpers.splitObject(data);

        mScreenId = Helpers.parseStr(split, 1);
    }

    private void persistData() {
        if (GlobalPreferences.sInstance == null) {
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceData(Helpers.mergeObject(null, mScreenId)); // null for ScreenItem
    }
}
