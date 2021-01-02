package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class AppInfo {
    /**
     * Return JS decipher function as string<br/>
     * Player path example: <b>/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("\"player_url\":\"([^\"]*)\"")
    private String mPlayerUrl;

    /**
     * Url for m=base script<br/>
     * Which contains client_secret and client_id constants
     */
    @RegExp({
            "id=\"base-js\" src=\"([^\"]*)\"",
            "\\.src = '([^']*m=base)'", // Cobalt path
            "\\.src = '([^']*)'; .\\.id = 'base-js'"}) // New Cobalt path
    private String mBaseUrl;

    public String getPlayerUrl() {
        return mPlayerUrl;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }
}
