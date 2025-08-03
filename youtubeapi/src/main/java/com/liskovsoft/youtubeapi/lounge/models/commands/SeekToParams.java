package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class SeekToParams {
    @JsonPath("$.newTime")
    private String mNewTimeSec;

    public String getNewTimeSec() {
        return mNewTimeSec;
    }
}
