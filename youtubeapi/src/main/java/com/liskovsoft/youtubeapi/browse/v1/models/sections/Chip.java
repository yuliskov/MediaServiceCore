package com.liskovsoft.youtubeapi.browse.v1.models.sections;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class Chip {
    @JsonPath("$.text.simpleText")
    private String mTitle;

    @JsonPath("$.content.horizontalListRenderer.continuations[0].reloadContinuationData.continuation")
    private String mReloadPageKey;

    @JsonPath("$.content.horizontalListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    // Next section presents only inside suggestions
    @JsonPath("$.content.horizontalListRenderer.items[*]")
    private List<ItemWrapper> mItemWrappers;

    public String getTitle() {
        return mTitle;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }
}
