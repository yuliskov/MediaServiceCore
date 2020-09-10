package com.liskovsoft.youtubeapi.browse.ver1.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class BrowseTab {
    @JsonPath("$.title")
    private String title;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String browseId;

    //@JsonPath("$.content.tvSurfaceContentRenderer.content.gridRenderer.items[*].gridVideoRenderer")
    //private List<VideoItem> mGridItems;

    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*].shelfRenderer")
    private List<BrowseSection> mSections;

    //@JsonPath({"$.content.tvSurfaceContentRenderer.content.sectionListRenderer.continuations[0].nextContinuationData.continuation",
    //           "$.content.tvSurfaceContentRenderer.content.gridRenderer.continuations[0].nextContinuationData.continuation"})
    //private String mNextPageKey;

    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadPageKey;

    public String getTitle() {
        return title;
    }

    public String getBrowseId() {
        return browseId;
    }

    public List<BrowseSection> getSections() {
        return mSections;
    }


    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}
