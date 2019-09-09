package com.liskovsoft.youtubeapi.subscriptions.models;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class NextSubscriptions {
    @JsonPath("$.continuationContents.gridContinuation.items[*].gridVideoRenderer")
    private List<BrowseVideoItem> mVideoItems;

    @JsonPath("$.continuationContents.gridContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<BrowseVideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
