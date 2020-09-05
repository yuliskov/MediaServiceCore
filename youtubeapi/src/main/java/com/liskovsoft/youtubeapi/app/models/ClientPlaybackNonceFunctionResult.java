package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class ClientPlaybackNonceFunctionResult {
    /**
     * Returns Client Playback Nonce (CPN) function used in tracking as string
     */
    @RegExp(";function [_$A-Za-z]{2}\\(\\)\\{if\\(window\\.crypto&&window\\.crypto\\.getRandomValues.*" +
            "\\nfunction [_$A-Za-z]{2}\\(\\)\\{for\\(var .*b\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return b\\.join\\(\"\"\\)\\}")
    private String mContent;

    public String getContent() {
        return mContent;
    }
}
