package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class NextSearchResult {
    @JsonPath("$.continuationContents.itemSectionContinuation.contents[*].compactVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.continuationContents.itemSectionContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
