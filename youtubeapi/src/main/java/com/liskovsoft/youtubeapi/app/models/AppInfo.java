package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

/**
 * Parser for https://www.youtube.com/tv
 */
public class AppInfo {
    /**
     * Return JS decipher function as string<br/>
     * Path example: <b>/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("\"player_url\":\"(.*?)\"")
    private String mPlayerUrl;

    /**
     * Url for m=base script<br/>
     * Which contains client_secret and client_id constants<br/>
     * Path example: <b>/s/_/kabuki/_/js/k=kabuki.base.en_US.8vees7yb36s.O/am=RAQAmAAQ/d=1/rs=ANjRhVmalTy3cHtUi1JaaLqkXmz43jeSJw/m=base</b>
     */
    @RegExp({
            "id=\"base-js\" src=\"(.*?)\"",
            "\\.src = '(.*?m=base)'", // Cobalt path
            "\\.src = '(.*?)'; .\\.id = 'base-js'"}) // New Cobalt path
    private String mClientUrl;

    /**
     * E.g. Cgs5azZUVjRoazRuNCiY8s6GBg%3D%3D
     */
    @RegExp("\"visitorData\":\"(.*?)\"")
    private String mVisitorData;

    public String getPlayerUrl() {
        return ServiceHelper.tidyUrl(mPlayerUrl);
    }

    public String getClientUrl() {
        return ServiceHelper.tidyUrl(mClientUrl);
    }

    public String getVisitorData() {
        return mVisitorData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AppInfo) {
            AppInfo target = (AppInfo) obj;
            return Helpers.equals(getPlayerUrl(), target.getPlayerUrl()) &&
                    Helpers.equals(getClientUrl(), target.getClientUrl()) &&
                    Helpers.equals(getVisitorData(), target.getVisitorData());
        }

        return super.equals(obj);
    }
}
