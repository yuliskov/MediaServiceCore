package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class BaseData {
    @RegExp({"clientId:\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[\\w]{2}:\"\\w+\"", // Cobalt
            "\"([-\\w]+\\.apps\\.googleusercontent\\.com)\",[\\w]{2}=\"\\w+\""})
    private String mClientId;

    @RegExp({"clientId:\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[\\w]{2}:\"(\\w+)\"", // Cobalt
            "\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[\\w]{2}=\"(\\w+)\""})
    private String mClientSecret;

    public String getClientId() {
        return mClientId;
    }

    public String getClientSecret() {
        return mClientSecret;
    }
}
