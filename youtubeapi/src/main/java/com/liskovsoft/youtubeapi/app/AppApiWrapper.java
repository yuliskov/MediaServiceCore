package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.clientdata.ClientData;
import com.liskovsoft.youtubeapi.app.models.clientdata.ModernClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import retrofit2.Call;
import retrofit2.Response;

public class AppApiWrapper {
    private final AppApi mAppApi;

    public AppApiWrapper() {
        mAppApi = RetrofitHelper.withRegExp(AppApi.class);
    }

    /**
     * Obtains info with respect of anonymous browsing data (visitor cookie)
     */
    public AppInfo getAppInfo(String userAgent) {
        String visitorCookie = GlobalPreferences.getVisitorCookie();
        Call<AppInfo> wrapper = mAppApi.getAppInfo(userAgent, visitorCookie);
        AppInfo result = null;

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
    
    public ClientData getBaseData(String baseUrl) {
        if (baseUrl == null) {
            return null;
        }

        Call<ModernClientData> wrapper = mAppApi.getModernClientData(baseUrl);
        ClientData baseData = RetrofitHelper.get(wrapper);

        // Seems that legacy script encountered.
        // Needed values is stored in main script, not in base.
        if (baseData == null) {
            baseData = RetrofitHelper.get(mAppApi.getLegacyClientData(getMainUrl(baseUrl)));
        }

        return baseData;
    }

    /**
     * Converts base script url to main script url
     */
    private static String getMainUrl(String baseUrl) {
        if (baseUrl == null) {
            return null;
        }

        return baseUrl
                .replace("/dg=0/", "/exm=base/ed=1/")
                .replace("/m=base", "/m=main");
    }
}
