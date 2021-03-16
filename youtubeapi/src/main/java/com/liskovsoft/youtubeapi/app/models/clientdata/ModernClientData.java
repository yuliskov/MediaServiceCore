package com.liskovsoft.youtubeapi.app.models.clientdata;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

/**
 * Data contained withing m=base js file<br/>
 * NOTE: Same pattern could be encountered 3 times or more<br/>
 * We're using the first one.
 */
public class ModernClientData implements ClientData {
    @RegExp("\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[$\\w]{2}=\"\\w+\"")
    private String mClientId;

    @RegExp("\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[$\\w]{2}=\"(\\w+)\"")
    private String mClientSecret;

    @Override
    public String getClientId() {
        return mClientId;
    }

    @Override
    public String getClientSecret() {
        return mClientSecret;
    }
}
