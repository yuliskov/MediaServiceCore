package com.liskovsoft.youtubeapi.app.models.cached;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.AppInfo;

public class AppInfoCached extends AppInfo {
    private static final String DELIM = "%aic%";
    private final String mPlayerUrl;
    private final String mClientUrl;
    private final String mVisitorData;
    private final long mCreationTimeMs;

    private AppInfoCached(String playerUrl, String clientUrl, String visitorData) {
        this(playerUrl, clientUrl, visitorData, System.currentTimeMillis());
    }

    private AppInfoCached(String playerUrl, String clientUrl, String visitorData, long creationTimeMs) {
        mPlayerUrl = playerUrl;
        mClientUrl = clientUrl;
        mVisitorData = visitorData;
        mCreationTimeMs = creationTimeMs;
    }

    public static AppInfoCached fromString(String spec) {
        if (spec == null) {
            return null;
        }

        String[] split = Helpers.split(DELIM, spec);

        String playerUrl = Helpers.parseStr(split, 0);
        String clientUrl = Helpers.parseStr(split, 1);
        String visitorData = Helpers.parseStr(split, 2);
        long creationTimeMs = Helpers.parseLong(split, 3);

        return new AppInfoCached(playerUrl, clientUrl, visitorData, creationTimeMs);
    }

    public static AppInfoCached from(AppInfo appInfo) {
        if (appInfo == null) {
            return null;
        }

        return new AppInfoCached(appInfo.getPlayerUrl(), appInfo.getClientUrl(), appInfo.getVisitorData());
    }

    @NonNull
    @Override
    public String toString() {
        return Helpers.merge(DELIM, mPlayerUrl, mClientUrl, mVisitorData, mCreationTimeMs);
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

    public long getCreationTimeMs() {
        return mCreationTimeMs;
    }

    public boolean validate() {
        return mPlayerUrl != null && mClientUrl != null && mVisitorData != null;
    }
}
