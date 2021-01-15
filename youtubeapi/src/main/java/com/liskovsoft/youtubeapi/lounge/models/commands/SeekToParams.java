package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class SeekToParams {
    @JsonPath("$.newTime")
    private String mNewTimeSec;

    public String getNewTimeSec() {
        return mNewTimeSec;
    }
}
