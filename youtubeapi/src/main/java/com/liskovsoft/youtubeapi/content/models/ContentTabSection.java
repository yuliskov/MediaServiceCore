package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.support.JsonPath;

import java.util.List;

public class ContentTabSection {
    @JsonPath("$.shelfRenderer.title.runs[0].text")
    String title;
    @JsonPath("$.shelfRenderer.content.horizontalListRenderer.items")
    List<VideoItem> items;
}
