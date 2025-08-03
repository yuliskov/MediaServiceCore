package com.liskovsoft.youtubeapi.lounge.models.bind;

import com.liskovsoft.googlecommon.common.converters.regexp.RegExp;

public class ScreenId {
    @RegExp(".*")
    private String mScreenId;

    public String getScreenId() {
        return mScreenId;
    }
}
