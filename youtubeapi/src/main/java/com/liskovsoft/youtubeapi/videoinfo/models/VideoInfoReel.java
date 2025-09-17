package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class VideoInfoReel {
    @JsonPath("$.playerResponse")
    private VideoInfo mVideoInfo;

    public VideoInfo getVideoInfo() {
        return mVideoInfo;
    }
}
