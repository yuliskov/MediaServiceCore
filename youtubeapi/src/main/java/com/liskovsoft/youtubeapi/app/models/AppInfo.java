package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class AppInfo {
    /**
     * Return JS decipher function as string<br/>
     * Player path example: <b>/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("\"player_url\":\"([^\"]*)\"")
    private String mPlayerUrl;

    public String getPlayerUrl() {
        return mPlayerUrl;
    }
}
