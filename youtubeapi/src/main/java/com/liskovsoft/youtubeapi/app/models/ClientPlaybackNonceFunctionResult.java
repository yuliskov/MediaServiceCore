package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class ClientPlaybackNonceFunctionResult {
    /**
     * Returns Client Playback Nonce (CPN) function used in tracking as string
     */
    @RegExp("\\nfunction [_$A-Za-z]\\(\\)\\{for\\(var .*b\\.push\\(\".*\"\\.charAt\\(.*\\)\\);return b\\.join\\(\"\"\\)\\}")
    private String mContent;

    public String getContent() {
        return mContent;
    }
}
