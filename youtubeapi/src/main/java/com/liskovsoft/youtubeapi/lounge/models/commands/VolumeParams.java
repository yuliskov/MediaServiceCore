package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class VolumeParams {
    @JsonPath("$.volume")
    private String mVolume;

    @JsonPath("$.delta")
    private String mDelta;

    public String getVolume() {
        return mVolume;
    }

    public String getDelta() {
        return mDelta;
    }
}
