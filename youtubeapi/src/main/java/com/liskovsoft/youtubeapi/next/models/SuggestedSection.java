package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class SuggestedSection {
    @JsonPath({"$.title.runs[0].text", "$.title.simpleText"})
    private String mTitle;
    @JsonPath("$.content.horizontalListRenderer.items[*].pivotVideoRenderer")
    private List<VideoItem> mVideoSuggestions;
    @JsonPath("$.content.horizontalListRenderer.continuations[*].nextContinuationData.continuation")
    private List<String> mNextPageKey;

    public String getTitle() {
        return mTitle;
    }

    public List<VideoItem> getVideoSuggestions() {
        return mVideoSuggestions;
    }

    public String getNextPageKey() {
        if (mNextPageKey == null || mNextPageKey.isEmpty()) {
            return null;
        }

        return mNextPageKey.get(0);
    }
}
