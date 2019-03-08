package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class SearchResult {
    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*].compactVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.contents.sectionListRenderer.continuations[0].reloadContinuationData.continuation")
    private String mReloadPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}
