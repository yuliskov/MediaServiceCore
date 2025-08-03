package com.liskovsoft.youtubeapi.next.v1.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

/**
 * Alt path to get like/subscribe status (when no such info in metadata section, e.g. YouTube Music items)
 */
public class ButtonStates {
    @JsonPath("$.subscribeButton.toggleButtonRenderer.isToggled")
    private Boolean mIsSubscribeToggled;

    @JsonPath("$.likeButton.toggleButtonRenderer.isToggled")
    private Boolean mIsLikeToggled;

    @JsonPath("$.dislikeButton.toggleButtonRenderer.isToggled")
    private Boolean mIsDislikeToggled;

    @JsonPath("$.channelButton.videoOwnerRenderer.navigationEndpoint.browseEndpoint.browseId")
    private String mChannelId;

    @JsonPath("$.channelButton.videoOwnerRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mChannelThumbnails;

    public Boolean isSubscribeToggled() {
        return mIsSubscribeToggled;
    }

    public Boolean isLikeToggled() {
        return mIsLikeToggled;
    }

    public Boolean isDislikeToggled() {
        return mIsDislikeToggled;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public List<Thumbnail> getChannelThumbnails() {
        return mChannelThumbnails;
    }
}
