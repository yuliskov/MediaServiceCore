package com.liskovsoft.youtubeapi.browse.models.grid;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class GridTab {
    private static final String PRESENTATION_NEW_CONTENT = "NEW_CONTENT";

    @JsonPath("$.title")
    private String mTitle;

    @JsonPath("$.accessibility.accessibilityData.label")
    private String mTitleAlt;

    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String mBrowseId;

    /**
     * Not used
     */
    @JsonPath("$.endpoint.browseEndpoint.params")
    private String mParams;

    @JsonPath("$.content.tvSurfaceContentRenderer.content.gridRenderer.items[*]")
    private List<ItemWrapper> mItemWrappers;

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

    /**
     * Channel mark that there is new content available
     */
    @JsonPath("$.presentationStyle.style")
    private String mPresentationStyle;

    public String getTitle() {
        return mTitle;
    }

    public String getTitleAlt() {
        return mTitleAlt;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
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

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }

    public boolean hasNewContent() {
        return PRESENTATION_NEW_CONTENT.equals(mPresentationStyle);
    }
}
