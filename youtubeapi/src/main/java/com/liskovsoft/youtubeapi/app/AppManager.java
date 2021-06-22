package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfo;
import com.liskovsoft.youtubeapi.app.models.clientdata.LegacyClientData;
import com.liskovsoft.youtubeapi.app.models.clientdata.ModernClientData;
import com.liskovsoft.youtubeapi.app.models.PlayerData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface AppManager {
    /**
     * Get player version that associated with specified user agent.<br/>
     * Note, that user agent should be obtained from the Smart TV device.<br/>
     * Player url example: <b>"player_url":"\/s\/player\/e49bfb00\/tv-player-ias.vflset\/tv-player-ias.js"</b><br/>
     * Lang change tv's url: https://www.youtube.com/tv?hrld=1
     */
    @GET("https://www.youtube.com/tv")
    Call<AppInfo> getAppInfo(@Header("User-Agent") String userAgent);

    /**
     * Returns useful player data such as Decipher function and Client Playback Nonce (CPN) constant
     */
    @GET
    Call<PlayerData> getPlayerData(@Url String playerUrl);

    /**
     * Contains constants used in Auth<br/>
     * Such as client_secret and client_id<br/>
     * Located inside main.js
     */
    @GET
    Call<LegacyClientData> getLegacyClientData(@Url String mainUrl);

    /**
     * Contains constants used in Auth<br/>
     * Such as client_secret and client_id<br/>
     * Located inside base.js
     */
    @GET
    Call<ModernClientData> getModernClientData(@Url String baseUrl);
}
