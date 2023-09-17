package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class VideoInfoHls {
    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mStoryboardSpec;

    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }

    public String getStoryboardSpec() {
        return mStoryboardSpec;
    }
}
