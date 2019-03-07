package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPathCollection;

public class ContentTab {
    @JsonPath("$.title")
    private String title;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String browseId;

    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*].shelfRenderer")
    private JsonPathCollection<ContentTabSection> sections = new JsonPathCollection<>(ContentTabSection.class); // type erase fix

    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextContinuation;

    @JsonPath("$.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadContinuation;

    public String getTitle() {
        return title;
    }

    public String getBrowseId() {
        return browseId;
    }

    public JsonPathCollection<ContentTabSection> getSections() {
        return sections;
    }


    public String getNextContinuation() {
        return mNextContinuation;
    }

    public String getReloadContinuation() {
        return mReloadContinuation;
    }
}
