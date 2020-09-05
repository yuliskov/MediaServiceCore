package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class WatchNextResultContinuation {
    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].pivotVideoRenderer")
    private List<SuggestedItem> mWatchNextItems;
    @JsonPath("$.continuationContents.horizontalListContinuation.continuations[1].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<SuggestedItem> getWatchNextItems() {
        return mWatchNextItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
