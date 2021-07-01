package com.liskovsoft.youtubeapi.app.models.clientdata;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

/**
 * Cobalt/Legacy<br/>
 * Contained withing m=main js file
 */
public class LegacyClientData implements ClientData {
    @RegExp("clientId:\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[$\\w]+:\"\\w+\"")
    private String mClientId;

    @RegExp("clientId:\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[$\\w]+:\"(\\w+)\"")
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
