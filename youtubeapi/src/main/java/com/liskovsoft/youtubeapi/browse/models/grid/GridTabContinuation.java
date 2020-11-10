package com.liskovsoft.youtubeapi.browse.models.grid;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

/**
 * Used in many places: history, subscribed content continuation and channel updates browsing
 */
public class GridTabContinuation {
    @JsonPath({"$.continuationContents.gridContinuation.items[*]",                                   // other grid like history, subscriptions
               "$.continuationContents.tvSurfaceContentContinuation.content.gridRenderer.items[*]"}) // user playlist
    private List<ItemWrapper> mItemWrappers;

    @JsonPath({"$.continuationContents.gridContinuation.continuations[0].nextContinuationData.continuation",              // other grid like history, subscriptions
               "$.continuationContents.tvSurfaceContentContinuation.content.gridRenderer.continuations[0].nextContinuationData.continuation"}) // user playlist
    private String mNextPageKey;

    /**
     * Subscribed channel updates with button on top
     */
    @JsonPath("$.continuationContents.tvSurfaceContentContinuation.header.tvSurfaceHeaderRenderer.buttons[0].buttonRenderer")
    private ChannelButton mChannelButton;

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }

    public ChannelButton getChannelButton() {
        return mChannelButton;
    }
}
