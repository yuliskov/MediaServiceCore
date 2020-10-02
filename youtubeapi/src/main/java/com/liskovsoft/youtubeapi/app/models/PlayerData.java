package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class PlayerData {
    /**
     * Return JS decipher function as string.<br/>
     * Used when deciphering music items.<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp("var [_$A-Za-z]{2}=\\{.*\\n.*\\n.*\\nfunction [_$A-Za-z]{2}\\(a\\)\\{.*a\\.split\\(\"\"\\).*;return a\\.join\\(\"\"\\)\\}")
    private String mDecipherFunction;
    
    /**
     * Return Client Playback Nonce (CPN) function that used in tracking as string.<br/>
     * A nonce is a unique value chosen by an entity in a protocol, and it is used to protect that entity against attacks which fall under the very large umbrella of "replay".<br/>
     * Player url example: <b>https://www.youtube.com/s/player/e49bfb00/tv-player-ias.vflset/tv-player-ias.js</b>
     */
    @RegExp(";function [_$A-Za-z]{2}\\(\\)\\{if\\(window\\.crypto&&window\\.crypto\\.getRandomValues.*" +
            "\\nfunction [_$A-Za-z]{2}\\(\\)\\{for\\(var .*b\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return b\\.join\\(\"\"\\)\\}")
    private String mClientPlaybackNonce;

    public String getDecipherFunction() {
        return mDecipherFunction;
    }

    public String getClientPlaybackNonce() {
        return mClientPlaybackNonce;
    }
}
