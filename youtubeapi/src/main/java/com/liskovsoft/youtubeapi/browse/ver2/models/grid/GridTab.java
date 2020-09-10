package com.liskovsoft.youtubeapi.browse.ver2.models.grid;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class GridTab {
    @JsonPath("$.title")
    private String mTitle;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String mBrowseId;

    /**
     * Not used
     */
    @JsonPath("$.endpoint.browseEndpoint.params")
    private String mParams;

    @JsonPath("$.content.tvSurfaceContentRenderer.content.gridRenderer.items[*].gridVideoRenderer")
    private List<VideoItem> mVideoItems;

    /**
     * Used in continue Tabs
     */
    @JsonPath("$.content.tvSurfaceContentRenderer.content.gridRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    /**
     * Used in query User Library: Playlist, Watch Later, My videos
     */
    @JsonPath("$.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadPageKey;

    /**
     * Marks tab after that should come Playlists
     */
    @JsonPath("$.unselectable")
    private boolean mUnselectable;

    public String getTitle() {
        return mTitle;
    }

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getBrowseId() {
        return mBrowseId;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    public String getParams() {
        return mParams;
    }

    public boolean isUnselectable() {
        return mUnselectable;
    }
}
