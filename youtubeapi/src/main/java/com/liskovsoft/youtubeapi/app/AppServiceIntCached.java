package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.models.cached.AppInfoCached;
import com.liskovsoft.youtubeapi.app.models.cached.ClientDataCached;
import com.liskovsoft.youtubeapi.app.models.cached.PlayerDataCached;
import com.liskovsoft.youtubeapi.app.nsig.NSigExtractor;
import com.liskovsoft.youtubeapi.service.YouTubeMediaItemService;

public class AppServiceIntCached extends AppServiceInt {
    private static final String TAG = AppServiceIntCached.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private AppInfoCached mAppInfo;
    private PlayerDataCached mPlayerData;
    private ClientDataCached mClientData;
    private NSigExtractor mNSigExtractor;
    private long mAppInfoUpdateTimeMs;
    private boolean mFallbackMode;
    private final Object mPlayerSync = new Object();

    @Override
    protected synchronized AppInfo getAppInfo(String userAgent) {
        if (mAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mAppInfo;
        }

        if (mFallbackMode && check(getData().getAppInfo())) {
            mAppInfo = getData().getAppInfo();
            mAppInfoUpdateTimeMs = System.currentTimeMillis();
            // Reset dependent objects
            mPlayerData = null;
            mClientData = null;
            return mAppInfo;
        }

        if (check(getData().getAppInfo()) && System.currentTimeMillis() - getData().getAppInfo().getCreationTimeMs() < CACHE_REFRESH_PERIOD_MS) {
            mAppInfo = getData().getAppInfo();
            mAppInfoUpdateTimeMs = getData().getAppInfo().getCreationTimeMs();
            // Reset dependent objects
            mPlayerData = null;
            mClientData = null;
            return mAppInfo;
        }

        Log.d(TAG, "updateAppInfoData");

        AppInfo appInfo = super.getAppInfo(userAgent);

        mAppInfo = AppInfoCached.from(appInfo);
        mAppInfoUpdateTimeMs = System.currentTimeMillis();
        // Reset dependent objects
        mPlayerData = null;
        mClientData = null;

        return mAppInfo;
    }

    @Override
    protected PlayerData getPlayerData(String playerUrl) {
        synchronized (mPlayerSync) {
            return getPlayerDataInt(playerUrl);
        }
    }

    private PlayerData getPlayerDataInt(String playerUrl) {
        if (isPlayerCacheActual()) {
            return mPlayerData;
        }

        // See https://github.com/yt-dlp/yt-dlp/issues/12398
        if (playerUrl.contains("/player_ias_tce.vflset/")) {
            Log.d(TAG, "Modifying tce player URL: %s", playerUrl);
            playerUrl = playerUrl.replace("/player_ias_tce.vflset/", "/player_ias.vflset/");
        }

        PlayerDataCached playerDataCached = getData().getPlayerData();

        if (playerDataCached != null && Helpers.equals(playerDataCached.getPlayerUrl(), playerUrl)) {
            mPlayerData = playerDataCached;

            persistPlayerDataOrFail();

            return mPlayerData;
        }

        Log.d(TAG, "updatePlayerData");

        PlayerData playerData = super.getPlayerData(playerUrl);

        mPlayerData = PlayerDataCached.from(playerUrl, playerData);

        persistPlayerDataOrFail();

        return mPlayerData;
    }

    private boolean updateNSigExtractor(String playerUrl) {
        if (mNSigExtractor != null && Helpers.equals(mNSigExtractor.getPlayerUrl(), playerUrl)) {
            return true;
        }

        YouTubeMediaItemService.instance().invalidateCache();
        try {
            mNSigExtractor = super.getNSigExtractor(playerUrl);
            return true;
        } catch (Throwable e) { // StackOverflowError | IllegalStateException
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public NSigExtractor getNSigExtractor(String playerUrl) {
        synchronized (mPlayerSync) {
            return mNSigExtractor;
        }
    }

    @Override
    protected synchronized ClientData getClientData(String clientUrl) {
        if (mClientData != null) {
            return mClientData;
        }

        ClientDataCached clientDataCached = getData().getClientData();

        if (clientDataCached != null && Helpers.equals(clientDataCached.getClientUrl(), clientUrl)) {
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
        if (mFallbackMode) {
            return;
        }

        mAppInfo = null;
        getData().setAppInfo(null);
    }

    @Override
    public boolean isPlayerCacheActual() {
        synchronized (mPlayerSync) {
            return mPlayerData != null;
        }
    }

    private boolean check(AppInfoCached appInfo) {
        return appInfo != null && appInfo.validate();
    }

    private boolean check(PlayerDataCached playerData) {
        return playerData != null && playerData.validate();
    }

    private boolean check(ClientDataCached clientData) {
        return clientData != null && clientData.validate();
    }

    private boolean checkNSig() {
        if (mPlayerData == null) {
            return false;
        }

        return updateNSigExtractor(mPlayerData.getPlayerUrl());
    }

    private void persistPlayerDataOrFail() {
        if (check(mAppInfo) && check(mPlayerData) && checkNSig()) {
            getData().setAppInfo(mAppInfo);
            getData().setPlayerData(mPlayerData);
        } else {
            mAppInfo = null;
            mFallbackMode = true;
        }
    }
}
