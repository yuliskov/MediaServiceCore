package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Response;

public class AppApiWrapper {
    private final AppApi mAppApi;

    public AppApiWrapper() {
        mAppApi = RetrofitHelper.create(AppApi.class);
    }

    /**
     * Obtains info with respect of anonymous browsing data (visitor cookie)
     */
    public AppInfo getAppInfo(String userAgent) {
        String visitorCookie = GlobalPreferences.getVisitorCookie();
        Call<AppInfo> wrapper = mAppApi.getAppInfo(userAgent, visitorCookie);
        AppInfo result = null;

        // visitorCookie obtained once per all app lifecycle?
        if (visitorCookie == null) {
            Response<AppInfo> response = RetrofitHelper.getResponse(wrapper);
            if (response != null) {
                GlobalPreferences.setVisitorCookie(RetrofitHelper.getCookie(response, AppConstants.VISITOR_COOKIE_NAME));
                result = response.body();
            }
        } else {
            result = RetrofitHelper.get(wrapper);
        }

        return result;
    }
    
    public PlayerData getPlayerData(String playerUrl) {
        if (playerUrl == null) {
            return null;
        }

        Call<PlayerData> wrapper = mAppApi.getPlayerData(playerUrl);
        return RetrofitHelper.get(wrapper);
    }
    
    public ClientData getClientData(String clientUrl) {
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
        GlobalPreferences.setVisitorCookie(null);
    }
}
