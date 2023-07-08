package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class VideoInfoPremium {
    @JsonPath("$.streamingData.hlsManifestUrl")
    private String mHlsManifestUrl;

    public String getHlsManifestUrl() {
        return mHlsManifestUrl;
    }
}
