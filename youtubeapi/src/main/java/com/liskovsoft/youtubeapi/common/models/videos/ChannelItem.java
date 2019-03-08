package com.liskovsoft.youtubeapi.common.models.videos;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class ChannelItem {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> thumbnails;
    @JsonPath("$.title.runs[0].text")
    private String title;
    @JsonPath("$.channelId")
    private String channelId;
    @JsonPath("$.navigationEndpoint.browseEndpoint.browseId")
    private String browseId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String videoCount;
    @JsonPath("$.subscriberCountText.runs[0].text")
    private String subscriberCount;

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getBrowseId() {
        return browseId;
    }

    public String getVideoCount() {
        return videoCount;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }
}
