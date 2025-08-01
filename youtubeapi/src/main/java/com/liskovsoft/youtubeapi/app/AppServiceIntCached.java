package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.cached.AppInfoCached;
import com.liskovsoft.youtubeapi.app.models.cached.ClientDataCached;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor;
import com.liskovsoft.youtubeapi.service.YouTubeMediaItemService;

public class AppServiceIntCached extends AppServiceInt {
    private static final String TAG = AppServiceIntCached.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private AppInfoCached mAppInfo;
    private ClientDataCached mClientData;
    private PlayerDataExtractor mPlayerDataExtractor;
    private long mAppInfoUpdateTimeMs;
    private final Object mPlayerSync = new Object();

    @Override
    protected synchronized AppInfo getAppInfo(String userAgent) {
        if (mAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mAppInfo;
        }

        Log.d(TAG, "updateAppInfoData");

        AppInfo appInfo = super.getAppInfo(userAgent);

        mAppInfo = AppInfoCached.from(appInfo);
        mAppInfoUpdateTimeMs = System.currentTimeMillis();

        return mAppInfo;
    }

    @Override
    public PlayerDataExtractor getPlayerDataExtractor(String playerUrl) {
        synchronized (mPlayerSync) {
            if (mPlayerDataExtractor != null && Helpers.equalsAny(playerUrl, mPlayerDataExtractor.getPlayerUrl(), getFailedPlayerUrl())) {
                return mPlayerDataExtractor;
            }

            YouTubeMediaItemService.instance().invalidateCache();
            try {
                mPlayerDataExtractor = super.getPlayerDataExtractor(playerUrl);
                if (mPlayerDataExtractor.validate()) {
                    if (check(mAppInfo)) {
                        getData().setAppInfo(mAppInfo);
                    }
                } else {
                    restoreSafePlayerVersion();
                }
            } catch (Throwable e) { // StackOverflowError | IllegalStateException
                e.printStackTrace();
                restoreSafePlayerVersion();
            }

            return mPlayerDataExtractor;
        }
    }

    private void restoreSafePlayerVersion() {
        getData().setFailedAppInfo(mAppInfo);
        if (check(getData().getAppInfo())) { // can restore?
            mPlayerDataExtractor = super.getPlayerDataExtractor(getData().getAppInfo().getPlayerUrl());
        } else {
            mPlayerDataExtractor = super.getPlayerDataExtractor(AppConstants.playerUrls.get(0));
        }
    }

    @Override
    protected synchronized ClientData getClientData(String clientUrl) {
        if (mClientData != null && Helpers.equals(clientUrl, mClientData.getClientUrl())) {
            return mClientData;
        }

        ClientDataCached clientDataCached = getData().getClientData();

        if (clientDataCached != null && Helpers.equals(clientUrl, clientDataCached.getClientUrl())) {
            mClientData = clientDataCached;
            return mClientData;
        }

        Log.d(TAG, "updateClientData");

        ClientData clientData = super.getClientData(clientUrl);

        mClientData = ClientDataCached.from(clientUrl, clientData);

        if (check(mClientData)) {
            getData().setClientData(mClientData);
        }

        return mClientData;
    }

    @Override
    public void invalidateCache() {
        mAppInfo = null;
        // Don't reset Player's cache. It's too heavy to recreate often.
        // Better do it inside MediaServiceData after the update
    }

    @Override
    public boolean isPlayerCacheActual() {
        synchronized (mPlayerSync) {
            return mPlayerDataExtractor != null;
        }
    }

    private boolean check(AppInfoCached appInfo) {
        return appInfo != null && appInfo.validate();
    }

    private boolean check(ClientDataCached clientData) {
        return clientData != null && clientData.validate();
    }

    private String getFailedPlayerUrl() {
        return getData().getFailedAppInfo() != null ? getData().getFailedAppInfo().getPlayerUrl() : null;
    }
}
