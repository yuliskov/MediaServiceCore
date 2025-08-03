package com.liskovsoft.youtubeapi.browse.v1.models.sections;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SectionTabContinuation {
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.continuationContents.sectionListContinuation.contents[*].shelfRenderer")
    private List<Section> mSections;

    @JsonPath("$.continuationContents.sectionListContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<Section> getSections() {
        return mSections;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
