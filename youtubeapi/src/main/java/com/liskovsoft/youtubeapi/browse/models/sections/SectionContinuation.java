package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class SectionContinuation {
    @JsonPath("$.continuationContents.horizontalListContinuation.items[*]")
    private List<ItemWrapper> mItemWrappers;

    @JsonPath("$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    /**
     * Generic wrapper if there's no continuation content
     */
    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
