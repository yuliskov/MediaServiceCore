package com.liskovsoft.youtubeapi.app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface AppManager {
    // window['loadParams'] = {
    //                "label_requested": "",
    //                "player_url": "\/s\/player\/e49bfb00\/tv-player-ias.vflset\/tv-player-ias.js",
    /**
     * Get player version that associated with specified user agent.<br/>
     * Note, that user agent should be obtained from the Smart TV device.
     */
    @GET("https://youtube.com/tv")
    Call<String> getPlayerUrl(@Header("User-Agent") String userAgent);

    /**
     * Return JS decipher function as string<br/>
     * Player full url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @GET("{player_url}")
    Call<String> getDecipherFunction(@Path("player_url") String playerUrl);
}
