package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

// root element: pivotChannelRenderer
public class ChannelItem {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.title.runs[0].text", "$.displayName.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.channelId", "$.navigationEndpoint.browseEndpoint.browseId"})
    private String mChannelId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String mVideoCount;
    @JsonPath("$.subscriberCountText.runs[0].text")
    private String mSubscriberCountText;

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public String getVideoCount() {
        return mVideoCount;
    }

    public String getSubscriberCountText() {
        return mSubscriberCountText;
    }
}
