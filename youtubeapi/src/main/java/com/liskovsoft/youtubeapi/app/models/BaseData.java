package com.liskovsoft.youtubeapi.app.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class BaseData {
    @RegExp("\"([-\\w]+\\.apps\\.googleusercontent\\.com)\"")
    private String mClientId;

    @RegExp("\"[-\\w]+\\.apps\\.googleusercontent\\.com\",[\\w]{2}=\"(\\w+)\"")
    private String mClientSecret;

    public String getClientId() {
        return mClientId;
    }

    public String getClientSecret() {
        return mClientSecret;
    }
}
