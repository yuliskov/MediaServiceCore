package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.playerdata.PlayerDataExtractor;
import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

import retrofit2.Call;
import retrofit2.Response;

public class AppServiceInt {
    private static final String TAG = AppServiceInt.class.getSimpleName();
    private final AppApi mAppApi;

    public AppServiceInt() {
        mAppApi = RetrofitHelper.create(AppApi.class);
    }

    /**
     * Obtains info with respect of anonymous browsing data (visitor cookie)
     */
    protected AppInfo getAppInfo(String userAgent) {
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
        return new PlayerDataExtractor(playerUrl);
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
        // TODO: NPE 1.6K!!!
        ClientData clientData = getClientData();
        return clientData != null ? clientData.getClientId() : null;
    }

    /**
     * Constant used in AuthApi
     */
    public String getClientSecret() {
        return getClientData() != null ? getClientData().getClientSecret() : null;
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        // TODO: NPE 300!!!
        return getAppInfoData() != null ? getAppInfoData().getVisitorData() : null;
    }

    public String getPlayerUrl() {
        // NOTE: NPE 2.5K
        //return getData().getPlayerUrl() != null ? getData().getPlayerUrl() : mCachedAppInfo != null ? mCachedAppInfo.getPlayerUrl() : null;
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
