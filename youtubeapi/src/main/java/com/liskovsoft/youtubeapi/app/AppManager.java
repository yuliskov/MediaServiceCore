package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.DecipherFunction;
import com.liskovsoft.youtubeapi.app.models.AppInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface AppManager {
    /**
     * Get player version that associated with specified user agent.<br/>
     * Note, that user agent should be obtained from the Smart TV device.<br/>
     * Player url example: <b>"player_url":"\/s\/player\/e49bfb00\/tv-player-ias.vflset\/tv-player-ias.js"</b>
     */
    @GET("https://youtube.com/tv")
    Call<AppInfo> getAppInfo(@Header("User-Agent") String userAgent);

    /**
     * Return JS decipher function as string<br/>
     * Player full url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @GET("https://www.youtube.com{player_url}")
    Call<DecipherFunction> getDecipherFunction(@Path("player_url") String playerUrl);
}
