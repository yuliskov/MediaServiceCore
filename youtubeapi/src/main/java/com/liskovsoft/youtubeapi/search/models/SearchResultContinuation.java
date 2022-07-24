package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TileItem;

import java.util.List;

public class SearchResultContinuation {
    @JsonPath({
            "$.continuationContents.horizontalListContinuation.items[*].tileRenderer", // V3
            "$.continuationContents.itemSectionContinuation.contents[*].tileRenderer" // V2
    })
    private List<TileItem> mTileItems;

    @JsonPath({"$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*].compactVideoRenderer", // V7
               "$.continuationContents.itemSectionContinuation.contents[*].compactVideoRenderer"}) // V6
    private List<VideoItem> mVideoItems;

    @JsonPath({"$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*].tvMusicVideoRenderer",
               "$.continuationContents.itemSectionContinuation.contents[*].tvMusicVideoRenderer"})
    private List<MusicItem> mMusicItems;

    @JsonPath({"$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*].compactChannelRenderer",
               "$.continuationContents.itemSectionContinuation.contents[*].compactChannelRenderer"})
    private List<ChannelItem> mChannelItems;

    @JsonPath({"$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*].compactRadioRenderer",
               "$.continuationContents.itemSectionContinuation.contents[*].compactRadioRenderer"})
    private List<RadioItem> mRadioItems;

    @JsonPath({"$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*].compactPlaylistRenderer",
               "$.continuationContents.itemSectionContinuation.contents[*].compactPlaylistRenderer"})
    private List<PlaylistItem> mPlaylistItems;

    @JsonPath({
            "$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation", // V3
            "$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation",
            "$.continuationContents.itemSectionContinuation.continuations[0].nextContinuationData.continuation"
    })
    private String mNextPageKey;

    /**
     * Presents even when there is no results
     */
    @JsonPath("$.estimatedResults")
    private String mEstimatedResults;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<ChannelItem> getChannelItems() {
        return mChannelItems;
    }

    public List<MusicItem> getMusicItems() {
        return mMusicItems;
    }

    public List<RadioItem> getRadioItems() {
        return mRadioItems;
    }

    public List<PlaylistItem> getPlaylistItems() {
        return mPlaylistItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getEstimatedResults() {
        return mEstimatedResults;
    }

    public List<TileItem> getTileItems() {
        return mTileItems;
    }
}
