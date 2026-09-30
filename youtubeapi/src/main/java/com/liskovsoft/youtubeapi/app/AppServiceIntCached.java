package com.liskovsoft.youtubeapi.app;

import androidx.annotation.Nullable;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.cached.AppInfoCached;
import com.liskovsoft.youtubeapi.app.models.cached.ClientDataCached;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.common.helpers.AppConstants;

public class AppServiceIntCached extends AppServiceInt {
    private static final String TAG = AppServiceIntCached.class.getSimpleName();
    private static final long CACHE_REFRESH_PERIOD_MS = 10 * 60 * 60 * 1_000; // check updated core files every 10 hours
    private AppInfoCached mAppInfo;
    private ClientDataCached mClientData;
    private PlayerDataExtractor mWebPlayerDataExtractor;
    private PlayerDataExtractor mTclPlayerDataExtractor;
    private long mAppInfoUpdateTimeMs;
    private final Object mAppInfoSync = new Object();
    private final Object mPlayerSync = new Object();
    private final Object mClientDataSync = new Object();
    private AppClient mRecentClient;

    @Override
    protected AppInfo getAppInfo(String userAgent) {
        synchronized (mAppInfoSync) {
            return getAppInfoSync(userAgent);
        }
    }

    private AppInfo getAppInfoSync(String userAgent) {
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
        return getPlayerDataExtractorSync(null, playerUrl);
    }

    @Override
    public PlayerDataExtractor getPlayerDataExtractor(@Nullable AppClient client) {
        return getPlayerDataExtractorSync(client, getPlayerUrl());
    }

    @Override
    public PlayerDataExtractor getPlayerDataExtractor(@Nullable AppClient client, String playerUrl) {
        return getPlayerDataExtractorSync(client, playerUrl);
    }

    private PlayerDataExtractor getPlayerDataExtractorSync(@Nullable AppClient client, String playerUrl) {
        synchronized (mPlayerSync) {
            return firstValidExtractor(
                    client,
                    playerUrl,
                    check(getData().getAppInfo()) ? getData().getAppInfo().getPlayerUrl() : null,
                    AppConstants.playerUrls.get(0)
            );
        }
    }

    @Override
    protected ClientData getClientData(String clientUrl) {
        synchronized (mClientDataSync) {
            return getClientDataSync(clientUrl);
        }
    }

    private ClientData getClientDataSync(String clientUrl) {
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
            return mWebPlayerDataExtractor != null || mTclPlayerDataExtractor != null;
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

    private PlayerDataExtractor firstValidExtractor(@Nullable AppClient client, String... playerUrls) {
        if (client == null) {
            client = mRecentClient;
        } else {
            mRecentClient = client;
        }

        int idx = -1;
        final int MAIN = 0;
        final int DATA = 1;
        final int APP_CONST = 2;
        String actualTimestamp = null;
        PlayerDataExtractor playerDataExtractor = restoreExtractor(client);

        if (playerDataExtractor != null && Helpers.equalsAny(playerUrls[MAIN], playerDataExtractor.getPlayerUrl(), getFailedPlayerUrl())) {
            return playerDataExtractor;
        }

        for (String url : playerUrls) {
            idx++;
            if (url == null) {
                continue;
            }

            playerDataExtractor = super.getPlayerDataExtractor(client, url);
            persistExtractor(client, playerDataExtractor);

            if (playerDataExtractor.validate()) {
                switch (idx) {
                    case MAIN:
                        getData().setAppInfo(mAppInfo);
                        getData().setFailedAppInfo(null);
                        break;
                    case DATA:
                    case APP_CONST:
                        getData().setFailedAppInfo(mAppInfo);
                        getData().setAppInfo(null);
                        break;
                }

                if (actualTimestamp != null) {
                    playerDataExtractor.setSignatureTimestamp(actualTimestamp);
                }

                break;
            }

            // Try to fetch the actual timestamp for old players. Needed for history (tracking) and possibly more.
            // NOTE: the older player may not work on newer timestamp
            if (idx == MAIN) {
                actualTimestamp = playerDataExtractor.getSignatureTimestamp();
            }
        }

        return playerDataExtractor;
    }

    private PlayerDataExtractor restoreExtractor(@Nullable AppClient client) {
        return isTcl(client) ? mTclPlayerDataExtractor : mWebPlayerDataExtractor;
    }

    private void persistExtractor(@Nullable AppClient client, PlayerDataExtractor extractor) {
        if (isTcl(client)) {
            mTclPlayerDataExtractor = extractor;
        } else {
            mWebPlayerDataExtractor = extractor;
        }
    }
}
