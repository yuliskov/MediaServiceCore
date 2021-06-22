package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

/**
 * Parser for https://youtube.com/tv
 */
public class AppInfo {
    /**
     * Return JS decipher function as string<br/>
     * Player path example: <b>/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("\"player_url\":\"([^\"]*)\"")
    private String mPlayerUrl = "/s/player/2fa3f946/tv-player-ias.vflset/tv-player-ias.js"; // TODO: remove when AppInfo NPE be fixed

    /**
     * Url for m=base script<br/>
     * Which contains client_secret and client_id constants
     */
    @RegExp({
            "id=\"base-js\" src=\"([^\"]*)\"",
            "\\.src = '([^']*m=base)'", // Cobalt path
            "\\.src = '([^']*)'; .\\.id = 'base-js'"}) // New Cobalt path
    private String mBaseUrl = "/s/_/kabuki/_/js/k=kabuki.base.en_US.AWbHXrvhM-E.O/am=RAABhAAQ/d=1/rs=ANjRhVkpjJ4qv11C3ADcDN7kWTKbNzemMQ/m=base"; // TODO: remove when AppInfo NPE be fixed

    public String getPlayerUrl() {
        return ServiceHelper.tidyUrl(mPlayerUrl);
    }

    public String getBaseUrl() {
        return ServiceHelper.tidyUrl(mBaseUrl);
    }
}
