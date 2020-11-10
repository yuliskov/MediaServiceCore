package com.liskovsoft.youtubeapi.browse.models.grid;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class ChannelButton {
    @JsonPath("$.text.simpleText")
    private String mButtonText;

    @JsonPath("$.navigationEndpoint.browseEndpoint.browseId")
    private String mBrowseId;

    @JsonPath("$.navigationEndpoint.browseEndpoint.canonicalBaseUrl")
    private String mCanonicalBaseUrl;

    public String getButtonText() {
        return mButtonText;
    }

    public String getBrowseId() {
        return mBrowseId;
    }

    public String getCanonicalBaseUrl() {
        return mCanonicalBaseUrl;
    }
}
