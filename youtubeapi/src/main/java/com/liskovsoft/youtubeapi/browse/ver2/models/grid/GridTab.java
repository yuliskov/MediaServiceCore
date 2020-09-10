package com.liskovsoft.youtubeapi.browse.ver2.models.grid;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class GridTab {
    @JsonPath("$.title")
    private String title;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String browseId;

    @JsonPath("$.content.tvSurfaceContentRenderer.content.gridRenderer.items[*].gridVideoRenderer")
    private List<VideoItem> mItems;

    @JsonPath("$.content.tvSurfaceContentRenderer.content.gridRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadPageKey;

    public String getTitle() {
        return title;
    }

    public List<VideoItem> getItems() {
        return mItems;
    }

    public String getBrowseId() {
        return browseId;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}
