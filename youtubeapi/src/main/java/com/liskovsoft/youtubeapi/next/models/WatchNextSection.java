package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class WatchNextSection {
    @JsonPath({"$.title.runs[0].text", "$.title.simpleText"})
    private String mTitle;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotVideoRenderer")
    private List<WatchNextItem> mWatchNextItems;
    @JsonPath("$.content.horizontalListRenderer.continuations[*].nextContinuationData.continuation")
    private List<String> mNextPageKey;

    public String getTitle() {
        return mTitle;
    }

    public List<WatchNextItem> getWatchNextItems() {
        return mWatchNextItems;
    }

    public String getNextPageKey() {
        if (mNextPageKey == null || mNextPageKey.isEmpty()) {
            return null;
        }

        return mNextPageKey.get(0);
    }
}
