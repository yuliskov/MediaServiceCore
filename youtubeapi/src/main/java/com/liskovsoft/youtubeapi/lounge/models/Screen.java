package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class Screen {
    @JsonPath("$.screenId")
    private String mScreenId;

    @JsonPath("$.loungeToken")
    private String mLoungeToken;

    public String getScreenId() {
        return mScreenId;
    }

    public String getLoungeToken() {
        return mLoungeToken;
    }
}
