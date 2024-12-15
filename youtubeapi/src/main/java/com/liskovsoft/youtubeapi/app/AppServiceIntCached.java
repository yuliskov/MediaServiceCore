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
    private final MediaServiceData mData;
    private AppInfoCached mAppInfo;
    private PlayerDataCached mPlayerData;
    private ClientDataCached mClientData;
    private NSigExtractor mNSigExtractor;
    private long mAppInfoUpdateTimeMs;
    private long mPlayerDataUpdateTimeMs;
    private long mClientDataUpdateTimeMs;
    private boolean mFallbackMode;

    public AppServiceIntCached() {
        mData = MediaServiceData.instance();
    }

    @Override
    public synchronized AppInfo getAppInfo(String userAgent) {
        if (mAppInfo != null && System.currentTimeMillis() - mAppInfoUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mAppInfo;
        }

        if (mFallbackMode && mData.getAppInfo() != null) {
            mAppInfo = mData.getAppInfo();
            mAppInfoUpdateTimeMs = System.currentTimeMillis();
            return mAppInfo;
        }

        Log.d(TAG, "updateAppInfoData");

        AppInfo appInfo = super.getAppInfo(userAgent);

        //if (!check(appInfo)) {
        //    if (mData.getAppInfo() != null) {
        //        mAppInfo = mData.getAppInfo();
        //        mAppInfoUpdateTimeMs = System.currentTimeMillis();
        //        return mAppInfo;
        //    }
        //}

        mAppInfo = AppInfoCached.from(appInfo);
        mAppInfoUpdateTimeMs = System.currentTimeMillis();

        return mAppInfo;
    }

    @Override
    public synchronized PlayerData getPlayerData(String playerUrl) {
        if (isPlayerCacheActual()) {
            return mPlayerData;
        }

        PlayerDataCached playerDataCached = mData.getPlayerData();

        if (playerDataCached != null && Helpers.equals(playerDataCached.getPlayerUrl(), playerUrl)) {
            mPlayerData = playerDataCached;
            mPlayerDataUpdateTimeMs = System.currentTimeMillis();

            persistPlayerDataOrFail();

            return mPlayerData;
        }

        Log.d(TAG, "updatePlayerData");

        PlayerData playerData = super.getPlayerData(playerUrl);

        //if (!check(playerData)) {
        //    if (playerDataCached != null) {
        //        mPlayerData = playerDataCached;
        //        mPlayerDataUpdateTimeMs = System.currentTimeMillis();
        //        updateNSigExtractor(playerDataCached.getPlayerUrl());
        //        return mPlayerData;
        //    }
        //}

        mPlayerData = PlayerDataCached.from(playerUrl, playerData);
        mPlayerDataUpdateTimeMs = System.currentTimeMillis();

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
        return mNSigExtractor;
    }

    @Override
    public synchronized ClientData getClientData(String clientUrl) {
        if (mClientData != null && System.currentTimeMillis() - mClientDataUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mClientData;
        }

        ClientDataCached clientDataCached = mData.getClientData();

        if (clientDataCached != null && Helpers.equals(clientDataCached.getClientUrl(), clientUrl)) {
            mClientData = clientDataCached;
            mClientDataUpdateTimeMs = System.currentTimeMillis();
            return mClientData;
        }

        Log.d(TAG, "updateClientData");

        ClientData clientData = super.getClientData(clientUrl);

        //if (!check(clientData)) {
        //    if (clientDataCached != null) {
        //        mClientData = clientDataCached;
        //        mClientDataUpdateTimeMs = System.currentTimeMillis();
        //        return mClientData;
        //    }
        //}

        mClientData = ClientDataCached.from(clientUrl, clientData);
        mClientDataUpdateTimeMs = System.currentTimeMillis();

        return mClientData;
    }

    @Override
    public void invalidateCache() {
        mAppInfo = null;
        mPlayerData = null;
        mClientData = null;
    }

    @Override
    public boolean isPlayerCacheActual() {
        return mPlayerData != null && System.currentTimeMillis() - mPlayerDataUpdateTimeMs < CACHE_REFRESH_PERIOD_MS;
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
        if (check(mAppInfo) && check(mPlayerData) && check(mClientData) && checkNSig()) {
            mData.setAppInfo(mAppInfo);
            mData.setPlayerData(mPlayerData);
            mData.setClientData(mClientData);
        } else {
            mAppInfo = null;
            mPlayerData = null;
            mClientData = null;
            mFallbackMode = true;
        }
    }
}
