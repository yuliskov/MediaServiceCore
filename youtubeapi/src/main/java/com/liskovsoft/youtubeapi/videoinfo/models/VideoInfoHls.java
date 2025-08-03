package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class VideoInfoHls {
    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    @JsonPath("$.storyboards.playerStoryboardSpecRenderer.spec")
    private String mStoryboardSpec;

    @JsonPath("$.streamingData.dashManifestUrl")
    private String mDashManifestUrl;

    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }

    public String getStoryboardSpec() {
        return mStoryboardSpec;
    }

    public String getDashManifestUrl() {
        return mDashManifestUrl;
    }
}
