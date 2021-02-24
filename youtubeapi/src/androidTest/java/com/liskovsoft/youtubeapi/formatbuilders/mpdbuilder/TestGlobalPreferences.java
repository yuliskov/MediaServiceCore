package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;

public class TestGlobalPreferences extends GlobalPreferences {
    private String mRawAuthData;

    @Override
    public void setRawAuthData(String data) {
        mRawAuthData = data;
    }

    @Override
    public String getRawAuthData() {
        return mRawAuthData;
    }
}
