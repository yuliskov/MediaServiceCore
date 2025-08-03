package com.liskovsoft.youtubeapi.browse.v1.models.grid;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class ChannelButton {
    @JsonPath("$.text.simpleText")
    private String mButtonText;

    @JsonPath("$.navigationEndpoint.browseEndpoint.browseId")
    private String mBrowseId;

    @JsonPath("$.navigationEndpoint.browseEndpoint.canonicalBaseUrl")
    private String mCanonicalBaseUrl;

    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String mVideoId; // first video

    @JsonPath("$.navigationEndpoint.watchEndpoint.params")
    private String mParams; // something like playlistId

    public String getButtonText() {
        return mButtonText;
    }

    public String getBrowseId() {
        return mBrowseId;
    }

    public String getCanonicalBaseUrl() {
        return mCanonicalBaseUrl;
    }

    /**
     * Could be used as a playlistId replacement
     */
    public String getParams() {
        return mParams;
    }

    /**
     * First video from the channel uploads
     */
    public String getVideoId() {
        return mVideoId;
    }
}
