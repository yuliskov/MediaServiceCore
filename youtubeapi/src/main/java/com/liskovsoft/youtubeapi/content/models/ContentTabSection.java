package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.common.models.videos.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.videos.MusicItem;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPathCollection;

public class ContentTabSection {
    @JsonPath("$.title.runs[0].text")
    private String title;
    @JsonPath("$.content.horizontalListRenderer.items[*].gridVideoRenderer")
    private JsonPathCollection<VideoItem> videoItems = new JsonPathCollection<>(VideoItem.class);
    @JsonPath("$.content.horizontalListRenderer.items[*].tvMusicVideoRenderer")
    private JsonPathCollection<MusicItem> musicItems = new JsonPathCollection<>(MusicItem.class);
    @JsonPath("$.content.horizontalListRenderer.items[*].gridChannelRenderer")
    private JsonPathCollection<ChannelItem> channelItems = new JsonPathCollection<>(ChannelItem.class);

    public String getTitle() {
        return title;
    }

    public JsonPathCollection<VideoItem> getVideoItems() {
        return videoItems;
    }

    public JsonPathCollection<MusicItem> getMusicItems() {
        return musicItems;
    }

    public JsonPathCollection<ChannelItem> getChannelItems() {
        return channelItems;
    }
}
