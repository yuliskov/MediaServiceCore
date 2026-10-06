package com.liskovsoft.youtubeapi.app;

import androidx.annotation.Nullable;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.TvConfig;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor;
import com.liskovsoft.youtubeapi.app.playerdata.TclPlayerDataExtractor;
import com.liskovsoft.youtubeapi.app.playerdata.WebPlayerDataExtractor;
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

import retrofit2.Call;
import retrofit2.Response;

class AppServiceCore {
    private static final String TAG = AppServiceCore.class.getSimpleName();
    private final AppApi mAppApi;

    public AppServiceCore() {
        mAppApi = RetrofitHelper.create(AppApi.class);
    }

    /**
     * Obtains info with respect of anonymous browsing data (visitor cookie)
     */
    public AppInfo getAppInfo(String userAgent) {
        String visitorCookie = getData().getVisitorCookie();
        Call<AppInfo> wrapper = mAppApi.getAppInfo(userAgent, visitorCookie);
        AppInfo result = null;

        Response<AppInfo> response = RetrofitHelper.getResponse(wrapper);

        if (response != null) {
            //String visitorInfoCookie = RetrofitHelper.getCookie(response, AppConstants.VISITOR_INFO_COOKIE);
            //String visitorPrivacyCookie = RetrofitHelper.getCookie(response, AppConstants.VISITOR_PRIVACY_COOKIE);
            //getData().setVisitorCookie(Helpers.join("; ", visitorInfoCookie, visitorPrivacyCookie));
            getData().setVisitorCookie(RetrofitHelper.getCookies(response));
            result = response.body();
        }

        return result;
    }

    public PlayerDataExtractor getPlayerDataExtractor(String playerUrl) {
        return getPlayerDataExtractor(null, playerUrl);
    }

    public PlayerDataExtractor getPlayerDataExtractor(@Nullable AppClient client) {
        return getPlayerDataExtractor(client, getPlayerUrl());
    }

    public PlayerDataExtractor getPlayerDataExtractor(@Nullable AppClient client, String playerUrl) {
        // Only the TCL flavored player (e.g. resolved from tv_config) has a different n-function shape.
        // Other urls (e.g. fallbacks) are regular players and should be solved the regular way, whatever the client is.
        return isTcl(client) ? new TclPlayerDataExtractor(playerUrl) : new WebPlayerDataExtractor(playerUrl);
    }

    public TvConfig getTvConfig() {
        Call<TvConfig> wrapper = mAppApi.getTvConfig();
        return RetrofitHelper.get(wrapper);
    }

    protected boolean isTcl(@Nullable AppClient client) {
        return client != null && client.isTVClient() && client != AppClient.TV_DOWNGRADED;
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
        getData().setVisitorCookie(null);
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
        ClientData clientData = getClientData();
        return clientData != null ? clientData.getClientId() : null;
    }

    /**
     * Constant used in AuthApi
     */
    public String getClientSecret() {
        ClientData clientData = getClientData();
        return clientData != null ? clientData.getClientSecret() : null;
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        AppInfo appInfoData = getAppInfoData();
        return appInfoData != null ? appInfoData.getVisitorData() : null;
    }

    public String getPlayerUrl() {
        AppInfo appInfoData = getAppInfoData();
        return appInfoData != null ? appInfoData.getPlayerUrl() : null;
    }

    public String getClientUrl() {
        AppInfo appInfoData = getAppInfoData();
        return appInfoData != null ? appInfoData.getClientUrl() : null;
    }

    private AppInfo getAppInfoData() {
        return getAppInfo(DefaultHeaders.APP_USER_AGENT);
    }

    private ClientData getClientData() {
        return getClientData(getClientUrl());
    }

    public PlayerDataExtractor getPlayerDataExtractor() {
        return getPlayerDataExtractor(getPlayerUrl());
    }

    public void refreshCacheIfNeeded() {
        getAppInfoData();
        getClientData();
        getPlayerDataExtractor();
    }

    protected MediaServiceData getData() {
        return MediaServiceData.instance();
    }
}
