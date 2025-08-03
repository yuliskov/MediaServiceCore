package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class SearchResult {
    //@JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*]")
    //private List<ItemWrapper> mItemWrappers;
    //
    ///**
    // * Nowadays, search result may contains two rows.<br/>
    // * First of them with continuation is interested for us.
    // */
    //@JsonPath({"$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation",
    //           "$.contents.sectionListRenderer.contents[1].itemSectionRenderer.continuations[0].nextContinuationData.continuation"})
    //private String mNextPageKey;

    /**
     * Search result with multiple rows
     */
    @JsonPath("$.contents.sectionListRenderer.contents[*]")
    private List<SearchSection> mSections;

    /**
     * Presents even when there is no results
     */
    @JsonPath("$.estimatedResults")
    private String mEstimatedResults;

    public String getNextPageKey() {
        return mSections != null && mSections.size() > 0 ? mSections.get(0).getNextPageKey() : null;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mSections != null && mSections.size() > 0 ? mSections.get(0).getItemWrappers() : null;
    }

    public List<SearchSection> getSections() {
        return mSections;
    }

    public String getEstimatedResults() {
        return mEstimatedResults;
    }
}
