package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class VolumeParams {
    @JsonPath("$.volume")
    private String mVolume;

    public String getVolume() {
        return mVolume;
    }
}
