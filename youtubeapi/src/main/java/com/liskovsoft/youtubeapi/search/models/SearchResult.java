package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.search.models.V2.TitleItem;

import java.util.List;

public class SearchResult {
    // V2
    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].tileRenderer")
    private List<TitleItem> mTitleItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].tvMusicVideoRenderer")
    private List<MusicItem> mMusicItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactChannelRenderer")
    private List<ChannelItem> mChannelItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactRadioRenderer")
    private List<RadioItem> mRadioItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactPlaylistRenderer")
    private List<PlaylistItem> mPlaylistItems;

    /**
     * Nowadays, search result may contains two rows.<br/>
     * First of them with continuation is interested for us.
     */
    @JsonPath({"$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation",
               "$.contents.sectionListRenderer.contents[1].itemSectionRenderer.continuations[0].nextContinuationData.continuation"})
    private String mNextPageKey;

    /**
     * Presents even when there is no results
     */
    @JsonPath("$.estimatedResults")
    private String mEstimatedResults;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<MusicItem> getMusicItems() {
        return mMusicItems;
    }

    public List<ChannelItem> getChannelItems() {
        return mChannelItems;
    }

    public List<RadioItem> getRadioItems() {
        return mRadioItems;
    }

    public List<TitleItem> getTitleItems() {
        return mTitleItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<PlaylistItem> getPlaylistItems() {
        return mPlaylistItems;
    }

    public String getEstimatedResults() {
        return mEstimatedResults;
    }
}
