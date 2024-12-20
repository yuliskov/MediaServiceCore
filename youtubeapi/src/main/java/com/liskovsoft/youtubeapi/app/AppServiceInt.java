package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.nsig.NSigExtractor;
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

import retrofit2.Call;
import retrofit2.Response;

public class AppServiceInt {
    private final AppApi mAppApi;

    public AppServiceInt() {
        mAppApi = RetrofitHelper.create(AppApi.class);
    }

    /**
     * Obtains info with respect of anonymous browsing data (visitor cookie)
     */
    protected AppInfo getAppInfo(String userAgent) {
        String visitorCookie = MediaServiceData.instance().getVisitorCookie();
        Call<AppInfo> wrapper = mAppApi.getAppInfo(userAgent, visitorCookie);
        AppInfo result = null;

        // visitorCookie obtained once per all app lifecycle?
        if (visitorCookie == null) {
            Response<AppInfo> response = RetrofitHelper.getResponse(wrapper);
            if (response != null) {
                MediaServiceData.instance().setVisitorCookie(RetrofitHelper.getCookie(response, AppConstants.VISITOR_COOKIE_NAME));
                result = response.body();
            }
        } else {
            result = RetrofitHelper.get(wrapper);
        }

        return result;
    }

    protected PlayerData getPlayerData(String playerUrl) {
        if (playerUrl == null) {
            return null;
        }

        Call<PlayerData> wrapper = mAppApi.getPlayerData(playerUrl);
        return RetrofitHelper.get(wrapper);
    }

    public NSigExtractor getNSigExtractor(String playerUrl) {
        return new NSigExtractor(playerUrl);
    }

    protected ClientData getClientData(String clientUrl) {
        if (clientUrl == null) {
            return null;
        }

        Call<ClientData> wrapper = mAppApi.getClientData(clientUrl);
        ClientData clientData = RetrofitHelper.get(wrapper);

        // Seems that legacy script encountered.
        if (clientData == null) {
            clientData = RetrofitHelper.get(mAppApi.getClientData(getLegacyClientUrl(clientUrl)));
        }

        return clientData;
    }
    
    private static String getLegacyClientUrl(String clientUrl) {
        if (clientUrl == null) {
            return null;
        }

        return clientUrl
                .replace("/dg=0/", "/exm=base/ed=1/")
                .replace("/m=base", "/m=main");
    }

    public void invalidateVisitorData() {
        MediaServiceData.instance().setVisitorCookie(null);
    }

    public void invalidateCache() {
        // NOP
    }

    public boolean isPlayerCacheActual() {
        // NOP
        return false;
    }

    // Moved from AppService

    public String getClientId() {
        // TODO: NPE 1.6K!!!
        return getClientData() != null ? getClientData().getClientId() : null;
    }

    /**
     * Constant used in AuthApi
     */
    public String getClientSecret() {
        return getClientData() != null ? getClientData().getClientSecret() : null;
    }

    /**
     * Used in get_video_info
     */
    public String getSignatureTimestamp() {
        // TODO: NPE 300!!!
        return getPlayerData() != null ? getPlayerData().getSignatureTimestamp() : null;
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        // TODO: NPE 300!!!
        return getAppInfoData() != null ? getAppInfoData().getVisitorData() : null;
    }

    public String getDecipherFunction() {
        return getPlayerData() != null ? getPlayerData().getDecipherFunction() : null;
    }

    public String getClientPlaybackNonceFunction() {
        // TODO: NPE 10K!!!
        PlayerData playerData = getPlayerData();
        return playerData != null ? playerData.getClientPlaybackNonceFunction() : null;
    }

    public String getPlayerUrl() {
        // NOTE: NPE 2.5K
        //MediaServiceData data = MediaServiceData.instance();
        //return data.getPlayerUrl() != null ? data.getPlayerUrl() : mCachedAppInfo != null ? mCachedAppInfo.getPlayerUrl() : null;
        return getAppInfoData() != null ? getAppInfoData().getPlayerUrl() : null;
    }

    public String getClientUrl() {
        // NOTE: NPE 143K!!!
        return getAppInfoData() != null ? getAppInfoData().getClientUrl() : null;
    }

    private AppInfo getAppInfoData() {
        return getAppInfo(DefaultHeaders.APP_USER_AGENT);
    }

    private ClientData getClientData() {
        return getClientData(getClientUrl());
    }

    private PlayerData getPlayerData() {
        return getPlayerData(getPlayerUrl());
    }

    public NSigExtractor getNSigExtractor() {
        if (getPlayerData() == null) {
            return null;
        }

        return getNSigExtractor(getPlayerUrl());
    }

    public void refreshCacheIfNeeded() {
        getAppInfoData();
        getPlayerData();
        getClientData();
    }
}
