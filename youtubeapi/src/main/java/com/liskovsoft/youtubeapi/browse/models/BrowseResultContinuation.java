package com.liskovsoft.youtubeapi.browse.models;

import com.liskovsoft.youtubeapi.common.models.videos.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class BrowseResultContinuation {
    @JsonPath({"$.continuationContents.horizontalListContinuation.items[*].gridVideoRenderer",
               "$.continuationContents.gridContinuation.items[*].gridVideoRenderer"})
    private List<VideoItem> mVideoItems;

    @JsonPath({"$.continuationContents.horizontalListContinuation.items[*].gridRadioRenderer",
               "$.continuationContents.gridContinuation.items[*].gridRadioRenderer"})
    private List<PlaylistItem> mPlaylistItems;

    @JsonPath({"$.continuationContents.gridContinuation.continuations[0].nextContinuationData.continuation",
               "$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation"})
    private String mNextPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<PlaylistItem> getPlaylistItems() {
        return mPlaylistItems;
    }
}
