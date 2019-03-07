package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPathCollection;

public class SearchResult {
    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactVideoRenderer")
    private JsonPathCollection<VideoItem> mVideoItems = new JsonPathCollection<>(VideoItem.class);

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.contents.sectionListRenderer.continuations[0].reloadContinuationData.continuation")
    private String mReloadPageKey;

    public JsonPathCollection<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}
