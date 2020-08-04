package com.liskovsoft.youtubeapi.videoinfo.models.formats;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class RegularFormat extends BaseVideoFormat {
    @JsonPath("$.audioQuality")
    private String mAudioQuality;

    public String getAudioQuality() {
        return mAudioQuality;
    }
}
