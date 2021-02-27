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
     * Subscribed channel updates with button on top<br/>
     * May have multiple buttons like "Play all", "Visit channel"
     */
    @JsonPath("$.continuationContents.tvSurfaceContentContinuation.header.tvSurfaceHeaderRenderer.buttons[*].buttonRenderer")
    private List<ChannelButton> mChannelButtons;

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }

    public List<ChannelButton> getChannelButtons() {
        return mChannelButtons;
    }

    /**
     * Channel GridTab contains channel id and other stuff
     */
    public String getBrowseId() {
        if (mChannelButtons != null) {
            for (ChannelButton button : mChannelButtons) {
                if (button.getBrowseId() != null) {
                    return button.getBrowseId();
                }
            }
        }

        return null;
    }

    /**
     * Could be used as a playlistId replacement
     */
    public String getParams() {
        if (mChannelButtons != null) {
            for (ChannelButton button : mChannelButtons) {
                if (button.getParams() != null) {
                    return button.getParams();
                }
            }
        }

        return null;
    }

    /**
     * Channel GridTab contains channel id and other stuff
     */
    public String getCanonicalBaseUrl() {
        if (mChannelButtons != null) {
            for (ChannelButton button : mChannelButtons) {
                if (button.getCanonicalBaseUrl() != null) {
                    return button.getCanonicalBaseUrl();
                }
            }
        }

        return null;
    }
}
