package com.liskovsoft.youtubeapi.content_old.models;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class ContentTab {
    @JsonPath("$.title")
    private String title;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String browseId;

    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*].shelfRenderer")
    private List<ContentTabSection> sections;

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

    public List<ContentTabSection> getSections() {
        return sections;
    }


    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}
