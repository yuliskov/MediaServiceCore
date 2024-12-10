package com.liskovsoft.youtubeapi.app.models.cached;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.AppInfo;

public class AppInfoCached extends AppInfo {
    private static final String DELIM = "%ai%";
    private final String mPlayerUrl;
    private final String mClientUrl;
    private final String mVisitorData;

    private AppInfoCached(String playerUrl, String clientUrl, String visitorData) {
        mPlayerUrl = playerUrl;
        mClientUrl = clientUrl;
        mVisitorData = visitorData;
    }

    public static AppInfo fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.split(DELIM, spec);

        return new AppInfoCached(Helpers.parseStr(split, 0), Helpers.parseStr(split, 1), Helpers.parseStr(split, 2));
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.merge(DELIM, mPlayerUrl, mClientUrl, mVisitorData);
    }

    @Override
    public String getPlayerUrl() {
        return mPlayerUrl;
    }

    @Override
    public String getClientUrl() {
        return mClientUrl;
    }

    @Override
    public String getVisitorData() {
        return mVisitorData;
    }
}
