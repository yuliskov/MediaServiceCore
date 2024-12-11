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

public class AppApiWrapperCached extends AppApiWrapper {
    private static final String TAG = AppApiWrapperCached.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private final MediaServiceData mData;
    private AppInfo mAppInfo;
    private PlayerData mPlayerData;
    private ClientData mClientData;
    private NSigExtractor mNSigExtractor;
    private long mAppInfoUpdateTimeMs;
    private long mPlayerDataUpdateTimeMs;
    private long mClientDataUpdateTimeMs;

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
            mData.setAppInfo(AppInfoCached.from(appInfo));
            mAppInfo = appInfo;
            mAppInfoUpdateTimeMs = System.currentTimeMillis();
        }

        return appInfo;
    }

    @Override
    public PlayerData getPlayerData(String playerUrl) {
        if (isPlayerCacheActual() || playerUrl == null) {
            return mPlayerData;
        }

        PlayerDataCached playerDataCached = mData.getPlayerData();

        if (playerDataCached != null && Helpers.equals(playerDataCached.getPlayerUrl(), playerUrl)) {
            mPlayerData = playerDataCached;
            mPlayerDataUpdateTimeMs = System.currentTimeMillis();
            updateNSigExtractor(playerUrl);
            return playerDataCached;
        }

        Log.d(TAG, "updatePlayerData");

        PlayerData playerData = super.getPlayerData(playerUrl);

        if (!check(playerData)) {
            playerData = playerDataCached != null ? playerDataCached : playerData;
        } else {
            mData.setPlayerData(PlayerDataCached.from(playerUrl, playerData));
            mPlayerData = playerData;
            mPlayerDataUpdateTimeMs = System.currentTimeMillis();
        }

        updateNSigExtractor(playerUrl);

        return playerData;
    }

    private void updateNSigExtractor(String playerUrl) {
        YouTubeMediaItemService.instance().invalidateCache();
        try {
            mNSigExtractor = super.getNSigExtractor(playerUrl);
        } catch (Throwable e) { // StackOverflowError | IllegalStateException
            e.printStackTrace();
            mPlayerData = null;
        }
    }

    @Override
    public NSigExtractor getNSigExtractor(String playerUrl) {
        return mNSigExtractor;
    }

    @Override
    public ClientData getClientData(String clientUrl) {
        if (mClientData != null && System.currentTimeMillis() - mClientDataUpdateTimeMs < CACHE_REFRESH_PERIOD_MS) {
            return mClientData;
        }

        ClientDataCached clientDataCached = mData.getClientData();

        if (clientDataCached != null && Helpers.equals(clientDataCached.getClientUrl(), clientUrl)) {
            mClientData = clientDataCached;
            mClientDataUpdateTimeMs = System.currentTimeMillis();
            return clientDataCached;
        }

        Log.d(TAG, "updateClientData");

        ClientData clientData = super.getClientData(clientUrl);

        if (!check(clientData)) {
            clientData = clientDataCached != null ? clientDataCached : clientData;
        } else {
            mData.setClientData(ClientDataCached.from(clientUrl, clientData));
            mClientData = clientData;
            mClientDataUpdateTimeMs = System.currentTimeMillis();
        }

        return clientData;
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
}
