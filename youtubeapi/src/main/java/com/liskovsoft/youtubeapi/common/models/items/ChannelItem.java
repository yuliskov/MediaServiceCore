package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class ChannelItem {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> thumbnails;
    @JsonPath({"$.title.runs[0].text", "$.displayName.runs[0].text"})
    private String title;
    @JsonPath({"$.channelId", "$.navigationEndpoint.browseEndpoint.browseId"})
    private String channelId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String videoCount;
    @JsonPath("$.subscriberCountText.runs[0].text")
    private String subscriberCountText;

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public String getSubscriberCountText() {
        return subscriberCountText;
    }
}
