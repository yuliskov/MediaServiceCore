package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class VoiceParams {
    public static final String STATUS_START = "INITIATED";
    public static final String STATUS_STOP = "CANCELED";
    @JsonPath("$.status")
    private String mStatus;

    public String getStatus() {
        return mStatus;
    }
}
