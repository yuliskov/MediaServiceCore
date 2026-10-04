package com.liskovsoft.youtubeapi.app.models;

import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;
import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;

/**
 * Parser for https://www.youtube.com/tv_config?action_get_config=true&client=lb4&theme=cl<br/>
 * Same endpoint the official TV app queries on startup. Commonly points to the
 * TCL player (<b>tv-player-*-tcl.vflset/tv-player-*-tcl.js</b>).
 */
public class TvConfig {
    /**
     * Path example: <b>/s/player/000000/tv-player-ias-tcl.vflset/tv-player-ias-tcl.js</b>
     */
    @RegExp("\"WEB_PLAYER_CONTEXT_CONFIG_ID_LIVING_ROOM_WATCH\":\\{.*?\"jsUrl\":\"(.*?)\"")
    private String mJsUrl;

    /**
     * BotGuard request key of the living room (TV) client. Differs from the web one.
     */
    @RegExp("\"challengeRequestKey\":\"(.*?)\"")
    private String mChallengeRequestKey;

    /**
     * Escaped JSON of the ready to use BotGuard challenge (contains <b>bgChallenge</b> object).
     */
    @RegExp("\"challengeParams\":\\{\"R\":\"(.*?[^\\\\])\",\"T\"")
    private String mChallengeRaw;

    public String getJsUrl() {
        return ServiceHelper.tidyUrl(mJsUrl);
    }

    public String getChallengeRequestKey() {
        return mChallengeRequestKey;
    }

    /**
     * @return unescaped challenge JSON or null
     */
    public String getChallenge() {
        if (mChallengeRaw == null) {
            return null;
        }
        try {
            return JsonParser.object().from("{\"challenge\":\"" + mChallengeRaw + "\"}").getString("challenge");
        } catch (JsonParserException e) {
            return null;
        }
    }

    /**
     * Whether the player url is the TCL one (different n-function code shape
     * than the regular web/TV player).
     */
    public static boolean isTclPlayerUrl(String playerUrl) {
        return playerUrl != null && playerUrl.contains("-tcl.js");
    }
}