package com.liskovsoft.youtubeapi.browse.ver1.models;

import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class BrowseResult {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[0].tabRenderer.content.tvSurfaceContentRenderer.content.gridRenderer.items[*].gridVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[0].tabRenderer.content.tvSurfaceContentRenderer.content.gridRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[0].tabRenderer.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadPageKey;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
