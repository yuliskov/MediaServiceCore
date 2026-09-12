package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

/**
 * Parser for https://www.youtube.com/tv_config?action_get_config=true&client=lb4&theme=cl
 * Same endpoint the official TV app queries on startup. Commonly points to the
 * TCL player (<b>tv-player-*-tcl.vflset/tv-player-*-tcl.js</b>).
 */
public class TvConfig {
    /**
     * Path example: <b>/s/player/000000/tv-player-ias-tcl.vflset/tv-player-ias-tcl.js</b>
     */
    @RegExp("\"WEB_PLAYER_CONTEXT_CONFIG_ID_LIVING_ROOM_WATCH\":\\{.*?\"jsUrl\":\"(.*?)\"")
    private String mJsUrl;

    public String getJsUrl() {
        return ServiceHelper.tidyUrl(mJsUrl);
    }

    /**
     * Whether the resolved player is the TCL one (different n-function code shape
     * than the regular web/TV player).
     */
    public boolean isTcl() {
        String jsUrl = getJsUrl();
        return jsUrl != null && jsUrl.contains("-tcl.js");
    }
}
