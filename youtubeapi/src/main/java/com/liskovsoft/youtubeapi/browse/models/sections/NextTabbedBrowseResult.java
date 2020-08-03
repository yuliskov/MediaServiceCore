package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class NextTabbedBrowseResult {
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.continuationContents.sectionListContinuation.contents[*].shelfRenderer")
    private List<BrowseSection> sections;

    @JsonPath("$.continuationContents.sectionListContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<BrowseSection> getSections() {
        return sections;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
