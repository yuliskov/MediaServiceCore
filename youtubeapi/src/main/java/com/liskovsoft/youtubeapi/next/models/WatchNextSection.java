package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class WatchNextSection {
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mTitle;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotVideoRenderer")
    private List<WatchNextItem> mWatchNextItems;
    @JsonPath("$.content.horizontalListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public String getTitle() {
        return mTitle;
    }

    public List<WatchNextItem> getWatchNextItems() {
        return mWatchNextItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
