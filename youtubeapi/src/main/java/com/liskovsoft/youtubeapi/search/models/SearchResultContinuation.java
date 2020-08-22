package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SearchResultContinuation {
    @JsonPath("$.continuationContents.itemSectionContinuation.contents[*].compactVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.continuationContents.itemSectionContinuation.contents[*].compactChannelRenderer")
    private List<ChannelItem> mChannelItems;

    @JsonPath("$.continuationContents.itemSectionContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<ChannelItem> getChannelItems() {
        return mChannelItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
