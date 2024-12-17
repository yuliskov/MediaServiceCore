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
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

public class AppServiceIntCached extends AppServiceInt {
    private static final String TAG = AppServiceIntCached.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private MediaServiceData mData;
    private AppInfoCached mAppInfo;
    private PlayerDataCached mPlayerData;
    private ClientDataCached mClientData;
    private NSigExtractor mNSigExtractor;
    private long mAppInfoUpdateTimeMs;
    private boolean mFallbackMode;
    private final Object mPlayerSync = new Object();

    @Override
    public synchronized AppInfo getAppInfo(String userAgent) {
        if (mAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mAppInfo;
        }

        if (mFallbackMode && getData().getAppInfo() != null) {
            mAppInfo = getData().getAppInfo();
            mAppInfoUpdateTimeMs = System.currentTimeMillis();
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
    public PlayerData getPlayerData(String playerUrl) {
        synchronized (mPlayerSync) {
            return getPlayerDataInt(playerUrl);
        }
    }

    private PlayerData getPlayerDataInt(String playerUrl) {
        if (isPlayerCacheActual()) {
            return mPlayerData;
        }

        PlayerDataCached playerDataCached = getData().getPlayerData();

        if (playerDataCached != null && Helpers.equals(playerDataCached.getPlayerUrl(), playerUrl)) {
            mPlayerData = playerDataCached;
            updateNSigExtractor(mPlayerData.getPlayerUrl());

            return mPlayerData;
        }

        Log.d(TAG, "updatePlayerData");

        PlayerData playerData = super.getPlayerData(playerUrl);

        mPlayerData = PlayerDataCached.from(playerUrl, playerData);

        persistPlayerDataOrFail();

        return mPlayerData;
    }

    private void updateNSigExtractor(String playerUrl) {
        if (mNSigExtractor != null && Helpers.equals(mNSigExtractor.getPlayerUrl(), playerUrl)) {
            return;
        }

        YouTubeMediaItemService.instance().invalidateCache();
        try {
            mNSigExtractor = super.getNSigExtractor(playerUrl);
        } catch (Throwable e) { // StackOverflowError | IllegalStateException
            e.printStackTrace();
            mAppInfo = null;
            mPlayerData = null;
            mClientData = null;
        }
    }

    @Override
    public NSigExtractor getNSigExtractor(String playerUrl) {
        synchronized (mPlayerSync) {
            return mNSigExtractor;
        }
    }

    @Override
    public synchronized ClientData getClientData(String clientUrl) {
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
        mAppInfo = null;
    }

    @Override
    public boolean isPlayerCacheActual() {
        synchronized (mPlayerSync) {
            return mPlayerData != null;
        }
    }

    private boolean check(AppInfo appInfo) {
        return appInfo != null && appInfo.getPlayerUrl() != null && appInfo.getClientUrl() != null && appInfo.getVisitorData() != null;
    }

    private boolean check(PlayerData playerData) {
        return playerData != null &&
                playerData.getClientPlaybackNonceFunction() != null &&
                playerData.getRawClientPlaybackNonceFunction() != null &&
                playerData.getDecipherFunction() != null &&
                playerData.getSignatureTimestamp() != null;
    }

    private boolean check(ClientData clientData) {
        return clientData != null && clientData.getClientId() != null && clientData.getClientSecret() != null;
    }

    private boolean checkNSig() {
        if (mPlayerData == null) {
            return false;
        }

        updateNSigExtractor(mPlayerData.getPlayerUrl());

        return mPlayerData != null;
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

    private MediaServiceData getData() {
        if (mData == null) {
            mData = MediaServiceData.instance();
        }

        return mData;
    }
}
