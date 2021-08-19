package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

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

    public Boolean getSubscribeToggled() {
        return mIsSubscribeToggled;
    }

    public Boolean getLikeToggled() {
        return mIsLikeToggled;
    }

    public Boolean getDislikeToggled() {
        return mIsDislikeToggled;
    }
}
