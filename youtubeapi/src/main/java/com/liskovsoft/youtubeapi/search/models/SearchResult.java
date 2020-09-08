package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SearchResult {
    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].tvMusicVideoRenderer")
    private List<MusicItem> mMusicItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactChannelRenderer")
    private List<ChannelItem> mChannelItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactRadioRenderer")
    private List<RadioItem> mPlaylistItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.contents.sectionListRenderer.continuations[0].reloadContinuationData.continuation")
    private String mReloadPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<MusicItem> getMusicItems() {
        return mMusicItems;
    }

    public List<ChannelItem> getChannelItems() {
        return mChannelItems;
    }

    public List<RadioItem> getPlaylistItems() {
        return mPlaylistItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}
