package com.liskovsoft.youtubeapi.content.models.videos;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

public class ChannelItem {
    @JsonPath("$.gridChannelRenderer.thumbnail.thumbnails[*]")
    private JsonPathCollection<Thumbnail> thumbnails = new JsonPathCollection<>(Thumbnail.class);
    @JsonPath("$.gridChannelRenderer.title.runs[0].text")
    private String title;
    @JsonPath("$.gridChannelRenderer.channelId")
    private String channelId;
    @JsonPath("$.gridChannelRenderer.navigationEndpoint.browseEndpoint.browseId")
    private String browseId;
    @JsonPath("$.gridChannelRenderer.videoCountText.runs[0].text")
    private String videoCount;
    @JsonPath("$.gridChannelRenderer.subscriberCountText.runs[0].text")
    private String subscriberCount;
}
