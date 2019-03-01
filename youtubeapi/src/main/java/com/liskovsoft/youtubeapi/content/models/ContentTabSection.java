package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

import java.util.List;

public class ContentTabSection {
    @JsonPath("$.shelfRenderer.title.runs[0].text")
    String title;
    @JsonPath("$.shelfRenderer.content.horizontalListRenderer.items[*]")
    JsonPathCollection<VideoItem> videoItems = new JsonPathCollection<>(VideoItem.class);
    @JsonPath("$.shelfRenderer.content.horizontalListRenderer.items[*]")
    JsonPathCollection<MusicItem> musicItems = new JsonPathCollection<>(MusicItem.class);
}
