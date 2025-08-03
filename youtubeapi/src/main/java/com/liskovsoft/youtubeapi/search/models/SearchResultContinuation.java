package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SearchResultContinuation {
    @JsonPath({
            // Tile items
            "$.continuationContents.sectionListContinuation.contents[0].shelfRenderer.content.horizontalListRenderer.items[*]", // V4
            "$.continuationContents.horizontalListContinuation.items[*]", // V3
            "$.continuationContents.itemSectionContinuation.contents[*]", // V2

            // Other items
            "$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*]" // V7
    })
    private List<ItemWrapper> mItemWrappers;

    @JsonPath({
            "$.continuationContents.sectionListContinuation.contents[0].shelfRenderer.content.horizontalListRenderer.continuations[0].nextContinuationData.continuation", // V4
            "$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation", // V3
            "$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.continuations[0].nextContinuationData.continuation",
            "$.continuationContents.itemSectionContinuation.continuations[0].nextContinuationData.continuation"
    })
    private String mNextPageKey;

    /**
     * Presents even when there is no results
     */
    @JsonPath("$.estimatedResults")
    private String mEstimatedResults;

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getEstimatedResults() {
        return mEstimatedResults;
    }
}
