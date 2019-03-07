package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPathCollection;

public class SearchResult {
    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactVideoRenderer")
    private JsonPathCollection<VideoItem> mResultList = new JsonPathCollection<>(VideoItem.class);

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextContinuation;

    @JsonPath("$.contents.sectionListRenderer.continuations[0].reloadContinuationData.continuation")
    private String mReloadContinuation;

    public JsonPathCollection<VideoItem> getResultList() {
        return mResultList;
    }

    public String getNextContinuation() {
        return mNextContinuation;
    }

    public String getReloadContinuation() {
        return mReloadContinuation;
    }
}
