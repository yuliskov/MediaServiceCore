package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.youtubeapi.app.nsig.NSigExtractor;
import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AppServiceInt {
    private static final String TAG = AppServiceInt.class.getSimpleName();
    private final AppApi mAppApi;
    private MediaServiceData mData;

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
            String visitorInfoCookie = RetrofitHelper.getCookie(response, AppConstants.VISITOR_INFO_COOKIE);
            String visitorPrivacyCookie = RetrofitHelper.getCookie(response, AppConstants.VISITOR_PRIVACY_COOKIE);
            getData().setVisitorCookie(Helpers.join("; ", visitorInfoCookie, visitorPrivacyCookie));
            result = response.body();
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
     * Used in get_video_info
     */
    public String getSignatureTimestamp() {
        // TODO: NPE 300!!!
        PlayerData playerData = getPlayerData();
        return playerData != null ? playerData.getSignatureTimestamp() : null;
    }

    /**
     * Used with get_video_info, anonymous search and suggestions
     */
    public String getVisitorData() {
        // TODO: NPE 300!!!
        return getAppInfoData() != null ? getAppInfoData().getVisitorData() : null;
    }

    private String getDecipherFunction() {
        return getPlayerData() != null ? getPlayerData().getDecipherFunction() : null;
    }

    public String getClientPlaybackNonceFunction() {
        // TODO: NPE 10K!!!
        PlayerData playerData = getPlayerData();
        return playerData != null ? playerData.getClientPlaybackNonceFunction() : null;
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

    public String createDecipherCode(List<String> ciphered) {
        String decipherFunction = getDecipherFunction();

        if (decipherFunction == null) {
            Log.e(TAG, "Oops. DecipherFunction is null...");
            return null;
        }

        StringBuilder result = new StringBuilder();
        result.append(decipherFunction);
        result.append("var result = [];");

        for (String cipher : ciphered) {
            result.append(String.format("result.push(decipherSignature('%s'));", cipher));
        }

        result.append("result.toString();");

        return result.toString();
    }

    protected MediaServiceData getData() {
        if (mData == null) {
            mData = MediaServiceData.instance();
        }

        return mData;
    }
}
