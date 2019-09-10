package com.liskovsoft.youtubeapi.browse.models;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class NextBrowseResult {
    @JsonPath({"$.continuationContents.gridContinuation.items[*].gridVideoRenderer",
               "$.continuationContents.horizontalListContinuation.items[*].gridVideoRenderer"})
    private List<VideoItem> mVideoItems;

    @JsonPath({"$.continuationContents.gridContinuation.continuations[0].nextContinuationData.continuation",
               "$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation"})
    private String mNextPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
