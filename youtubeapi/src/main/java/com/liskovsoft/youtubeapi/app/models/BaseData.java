package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class BaseData {
    @RegExp({
            "\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",\\n[\\w]{2}=\"\\w+\"", // Cobalt/Legacy
            "\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[\\w]{2}=\"\\w+\"", // Modern (always presents)
            "clientId:\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[\\w]{2}:\"\\w+\"" // Cobalt/Legacy (always presents)
            })
    private String mClientId;

    @RegExp({
            "\"[-\\w]+\\.apps\\.googleusercontent\\.com\",\\n[\\w]{2}=\"(\\w+)\"", // Cobalt/Legacy
            "\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[\\w]{2}=\"(\\w+)\"", // Modern (always presents)
            "clientId:\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[\\w]{2}:\"(\\w+)\"" // Cobalt/Legacy (always presents)
            })
    private String mClientSecret;

    public String getClientId() {
        return mClientId;
    }

    public String getClientSecret() {
        return mClientSecret;
    }
}
