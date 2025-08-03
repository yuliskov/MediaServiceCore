package com.liskovsoft.youtubeapi.browse.v1.models.grid;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.Collections;
import java.util.List;

/**
 * Used in many places: history, subscribed content continuation and channel updates browsing
 */
public class GridTabContinuation {
    @JsonPath({"$.continuationContents.gridContinuation.items[*]",                                   // other grid like history, subscriptions
               "$.continuationContents.tvSurfaceContentContinuation.content.gridRenderer.items[*]", // user playlist
               "$.continuationContents.sectionListContinuation.contents[0].itemSectionRenderer.contents[*]" // web client version history (with remove from history)
    })
    private List<ItemWrapper> mItemWrappers;

    @JsonPath({"$.continuationContents.gridContinuation.continuations[0].nextContinuationData.continuation",              // other grid like history, subscriptions
               "$.continuationContents.tvSurfaceContentContinuation.content.gridRenderer.continuations[0].nextContinuationData.continuation", // user playlist
               "$.continuationContents.sectionListContinuation.continuations[0].nextContinuationData.continuation" // web client version history (with remove from history)
    })
    private String mNextPageKey;

    /**
     * Subscribed channel updates with button on top<br/>
     * May have multiple buttons like "Play all", "Visit channel"
     */
    @JsonPath("$.continuationContents.tvSurfaceContentContinuation.header.tvSurfaceHeaderRenderer.buttons[*].buttonRenderer")
    private List<ChannelButton> mChannelButtons;

    /**
     * Generic button when channel has no content.
     */
    @JsonPath("$.continuationContents.tvSurfaceContentContinuation.content.genericPromoRenderer.actionButton.buttonRenderer")
    private ChannelButton mEmptyChannelButton;

    /**
     * Generic wrapper if there's no continuation content
     */
    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public void setNextPageKey(String nextPageKey) {
        mNextPageKey = nextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }

    public void setItemWrappers(List<ItemWrapper> itemWrappers) {
        mItemWrappers = itemWrappers;
    }

    public List<ChannelButton> getChannelButtons() {
        if (mChannelButtons != null) {
            return mChannelButtons;
        }

        // Generic button when channel has no content
        if (mEmptyChannelButton != null) {
            return Collections.singletonList(mEmptyChannelButton);
        }

        return null;
    }

    /**
     * Channel GridTab contains channel id and other stuff
     */
    public String getBrowseId() {
        if (getChannelButtons() != null) {
            for (ChannelButton button : getChannelButtons()) {
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
        if (getChannelButtons() != null) {
            for (ChannelButton button : getChannelButtons()) {
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
        if (getChannelButtons() != null) {
            for (ChannelButton button : getChannelButtons()) {
                if (button.getCanonicalBaseUrl() != null) {
                    return button.getCanonicalBaseUrl();
                }
            }
        }

        return null;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
