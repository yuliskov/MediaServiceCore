package com.liskovsoft.youtubeapi.app;

import com.liskovsoft.youtubeapi.app.models.AppInfoResult;
import com.liskovsoft.youtubeapi.app.models.DecipherFunctionResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Url;

public interface AppManager {
    /**
     * Get player version that associated with specified user agent.<br/>
     * Note, that user agent should be obtained from the Smart TV device.<br/>
     * Player url example: <b>"player_url":"\/s\/player\/e49bfb00\/tv-player-ias.vflset\/tv-player-ias.js"</b>
     */
    @GET("https://youtube.com/tv")
    Call<AppInfoResult> getAppInfo(@Header("User-Agent") String userAgent);

    /**
     * Return JS decipher function as string<br/>
     * Player full url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @GET
    Call<DecipherFunctionResult> getDecipherFunction(@Url String playerUrl);
}
