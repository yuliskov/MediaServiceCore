package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;

import java.util.List;

public class SectionContinuation {
    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].gridVideoRenderer")
    private List<VideoItem> mVideoItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.items[*].gridRadioRenderer")
    private List<RadioItem> mPlaylistItems;

    @JsonPath("$.continuationContents.horizontalListContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<VideoItem> getVideoItems() {
        return mVideoItems;
    }

    public List<RadioItem> getPlaylistItems() {
        return mPlaylistItems;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}
