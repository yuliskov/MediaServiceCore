package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

/**
 * Parser for https://www.youtube.com/tv
 */
public class AppInfo {
    /**
     * Return JS decipher function as string<br/>
     * Path example: <b>/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("\"player_url\":\"(.*?)\"")
    private String mPlayerUrl = "/s/player/7acefd5d/tv-player-ias.vflset/tv-player-ias.js"; // TODO: remove when AppInfo NPE be fixed

    /**
     * Url for m=base script<br/>
     * Which contains client_secret and client_id constants<br/>
     * Path example: <b>/s/_/kabuki/_/js/k=kabuki.base.en_US.8vees7yb36s.O/am=RAQAmAAQ/d=1/rs=ANjRhVmalTy3cHtUi1JaaLqkXmz43jeSJw/m=base</b>
     */
    @RegExp({
            "id=\"base-js\" src=\"(.*?)\"",
            "\\.src = '(.*?m=base)'", // Cobalt path
            "\\.src = '(.*?)'; .\\.id = 'base-js'"}) // New Cobalt path
    private String mBaseUrl = "/s/_/kabuki/_/js/k=kabuki.base.en_US.8vees7yb36s.O/am=RAQAmAAQ/d=1/rs=ANjRhVmalTy3cHtUi1JaaLqkXmz43jeSJw/m=base"; // TODO: remove when AppInfo NPE be fixed

    @RegExp("\"visitorData\":\"(.*?)\"")
    private String mVisitorData = "Cgs5azZUVjRoazRuNCiY8s6GBg%3D%3D"; // TODO: remove when AppInfo NPE be fixed

    public String getPlayerUrl() {
        return ServiceHelper.tidyUrl(mPlayerUrl);
    }

    public String getBaseUrl() {
        return ServiceHelper.tidyUrl(mBaseUrl);
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
