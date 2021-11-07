package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class SearchResult {
    @JsonPath("$.contents.sectionListRenderer.contents[0].itemSectionRenderer.contents[*]")
    private List<ItemWrapper> mItemWrappers;

    /**
     * Nowadays, search result may contains two rows.<br/>
     * First of them with continuation is interested for us.
     */
    @JsonPath({"$.contents.sectionListRenderer.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation",
               "$.contents.sectionListRenderer.contents[1].itemSectionRenderer.continuations[0].nextContinuationData.continuation"})
    private String mNextPageKey;

    /**
     * Presents even when there is no results
     */
    @JsonPath("$.estimatedResults")
    private String mEstimatedResults;

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getEstimatedResults() {
        return mEstimatedResults;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }
}
