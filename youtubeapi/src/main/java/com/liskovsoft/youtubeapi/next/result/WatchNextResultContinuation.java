package com.liskovsoft.youtubeapi.next.result;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class WatchNextResultContinuation {
    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].pivotVideoRenderer")
    private List<VideoItem> mWatchNextItems;
    @JsonPath("$.continuationContents.horizontalListContinuation.continuations[1].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<VideoItem> getVideoSuggestions() {
        return mWatchNextItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
