package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.ClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import com.liskovsoft.googlecommon.common.converters.regexp.WithRegExp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

@WithRegExp
public interface AppApi {
    /**
     * Get player version that associated with specified user agent.<br/>
     * Note, that user agent should be obtained from the Smart TV device.<br/>
     * Player url example: <b>"player_url":"\/s\/player\/e49bfb00\/tv-player-ias.vflset\/tv-player-ias.js"</b><br/>
     * Lang change tv's url: https://www.youtube.com/tv?hrld=1
     */
    @GET("https://www.youtube.com/tv")
    Call<AppInfo> getAppInfo(@Header("User-Agent") String userAgent);

    /**
     * Get player version that associated with specified user agent.<br/>
     * Note, that user agent should be obtained from the Smart TV device.<br/>
     * Note, 'visitorInfoLive' is used to obtain right 'googleVisitorId' (preserves anonymous history trace).<br/>
     * Player url example: <b>"player_url":"\/s\/player\/e49bfb00\/tv-player-ias.vflset\/tv-player-ias.js"</b><br/>
     * Lang change tv's url: https://www.youtube.com/tv?hrld=1
     */
    @GET("https://www.youtube.com/tv")
    Call<AppInfo> getAppInfo(@Header("User-Agent") String userAgent, @Header("Cookie") String visitorInfoLive);

    /**
     * Returns useful player data such as Decipher function and Client Playback Nonce (CPN) constant
     */
    @GET
    Call<PlayerData> getPlayerData(@Url String playerUrl);

    /**
     * Contains constants used in Auth<br/>
     * Such as client_secret and client_id<br/>
     * Located inside base.js (modern clients) or main.js (Cobalt/Legacy)
     */
    @GET
    Call<ClientData> getClientData(@Url String clientUrl);
}
