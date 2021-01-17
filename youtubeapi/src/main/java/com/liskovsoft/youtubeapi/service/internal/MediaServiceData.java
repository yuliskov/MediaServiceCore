package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.lounge.models.info.ScreenItem;

public class MediaServiceData {
    private static final String TAG = MediaServiceData.class.getSimpleName();
    private static MediaServiceData sInstance;
    private ScreenItem mScreenItem;

    private MediaServiceData() {
        restoreData();
    }

    public static MediaServiceData instance() {
        if (sInstance == null) {
            sInstance = new MediaServiceData();
        }

        return sInstance;
    }

    public ScreenItem getScreen() {
        return mScreenItem;
    }

    public void setScreen(ScreenItem screenItem) {
        mScreenItem = screenItem;
        persistData();
    }

    private void restoreData() {
        if (GlobalPreferences.sInstance == null) {
            Log.e(TAG, "Can't restore data. GlobalPreferences isn't initialized yet.");
            return;
        }

        String data = GlobalPreferences.sInstance.getMediaServiceData();

        String[] split = Helpers.splitObject(data);

        mScreenItem = ScreenItem.from(Helpers.parseStr(split, 0));
    }

    private void persistData() {
        if (GlobalPreferences.sInstance == null) {
            return;
        }

        GlobalPreferences.sInstance.setMediaServiceData(Helpers.mergeObject(Helpers.toString(mScreenItem)));
    }
}
