package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.content.models.videos.ChannelItem;
import com.liskovsoft.youtubeapi.content.models.videos.MusicItem;
import com.liskovsoft.youtubeapi.content.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

public class ContentTabSection {
    @JsonPath("$.shelfRenderer.title.runs[0].text")
    String title;
    @JsonPath("$.shelfRenderer.content.horizontalListRenderer.items[*]")
    JsonPathCollection<VideoItem> videoItems = new JsonPathCollection<>(VideoItem.class);
    @JsonPath("$.shelfRenderer.content.horizontalListRenderer.items[*]")
    JsonPathCollection<MusicItem> musicItems = new JsonPathCollection<>(MusicItem.class);
    @JsonPath("$.shelfRenderer.content.horizontalListRenderer.items[*]")
    JsonPathCollection<ChannelItem> channelItems = new JsonPathCollection<>(ChannelItem.class);
}
