package com.liskovsoft.youtubeapi.lounge.models;

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExp;

public class ScreenIdResult {
    @RegExp(".*")
    private String mScreenId;

    public String getScreenId() {
        return mScreenId;
    }
}
