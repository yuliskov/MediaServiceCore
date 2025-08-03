package com.liskovsoft.youtubeapi.next.v1.result;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class WatchNextResultContinuation {
    @JsonPath("$.continuationContents.horizontalListContinuation.items[*]")
    private List<ItemWrapper> mItemWrappers;
    @JsonPath({"$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation",
               "$.continuationContents.horizontalListContinuation.continuations[1].nextContinuationData.continuation"})
    private String mNextPageKey;

    /**
     * Marker field to avoid json parser empty result warning.<br/>
     * It appeared when we reached end of the list.<br/>
     */
    @JsonPath("$.contents.singleColumnWatchNextResults.results.results.trackingParams")
    private String mTrackingParams;

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }
}
