package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

public class AppApiWrapperCached extends AppApiWrapper {
    private static final String TAG = AppApiWrapperCached.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private AppInfo mAppInfo;
    private PlayerData mPlayerData;
    private ClientData mClientData;
    private long mAppInfoUpdateTimeMs;
    private long mPlayerDataUpdateTimeMs;
    private long mClientDataUpdateTimeMs;

    private final MediaServiceData mData;

    public AppApiWrapperCached() {
        mData = MediaServiceData.instance();
    }

    @Override
    public AppInfo getAppInfo(String userAgent) {
        if (mAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mAppInfo;
        }

        Log.d(TAG, "updateAppInfoData");

        AppInfo appInfo = super.getAppInfo(userAgent);

        if (!check(appInfo)) {
            appInfo = mData.getAppInfo() != null ? mData.getAppInfo() : appInfo;
        } else {
            mData.setAppInfo(appInfo);
            mAppInfo = appInfo;
            mAppInfoUpdateTimeMs = System.currentTimeMillis();
        }

        return appInfo;
    }

    @Override
    public PlayerData getPlayerData(String playerUrl) {
        return super.getPlayerData(playerUrl);
    }

    @Override
    public ClientData getClientData(String clientUrl) {
        return super.getClientData(clientUrl);
    }

    @Override
    public void invalidateCache() {
        mAppInfo = null;
        mPlayerData = null;
        mClientData = null;
    }

    private boolean check(AppInfo appInfo) {
        return appInfo != null && appInfo.getPlayerUrl() != null && appInfo.getClientUrl() != null && appInfo.getVisitorData() != null;
    }
}
