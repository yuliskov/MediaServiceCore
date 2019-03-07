package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPathCollection;

public class NextSearchResult {
    @JsonPath("$.continuationContents.itemSectionContinuation.contents[*].compactVideoRenderer")
    private JsonPathCollection<VideoItem> mVideoItems = new JsonPathCollection<>(VideoItem.class);

    @JsonPath("$.continuationContents.itemSectionContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public JsonPathCollection<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
