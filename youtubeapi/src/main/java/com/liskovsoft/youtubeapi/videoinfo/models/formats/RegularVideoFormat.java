package com.liskovsoft.youtubeapi.videoinfo.models.formats;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class RegularVideoFormat extends VideoFormat {
    @JsonPath("$.audioQuality")
    private String mAudioQuality;

    public String getAudioQuality() {
        return mAudioQuality;
    }
}
