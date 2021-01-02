package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class ScreenId {
    @RegExp(".*")
    private String mScreenId;

    public String getScreenId() {
        return mScreenId;
    }
}
