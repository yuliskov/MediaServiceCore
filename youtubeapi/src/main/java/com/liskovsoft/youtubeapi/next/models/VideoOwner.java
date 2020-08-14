package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class VideoOwner {
    @JsonPath("$.title.runs[0].text")
    private String mVideoAuthor;
    @JsonPath("$.subscribeButton.subscribeButtonRenderer.subscribed")
    private boolean mIsSubscribed;
    @JsonPath("$.subscribeButton.subscribeButtonRenderer.channelId")
    private String mChannelId;
    @JsonPath("$.subscribeButton.subscribeButtonRenderer.subscriberCountText.runs[0].text")
    private String mSubscriberCount;
    @JsonPath("$.subscribeButton.subscribeButtonRenderer.longSubscriberCountText.runs[0].text")
    private String mSubscriberCountLong;
    @JsonPath("$.subscribeButton.subscribeButtonRenderer.shortSubscriberCountText.runs[0].text")
    private String mSubscriberCountShort;

    public String getVideoAuthor() {
        return mVideoAuthor;
    }

    public boolean isSubscribed() {
        return mIsSubscribed;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public String getSubscriberCount() {
        return mSubscriberCount;
    }

    public String getSubscriberCountLong() {
        return mSubscriberCountLong;
    }

    public String getSubscriberCountShort() {
        return mSubscriberCountShort;
    }
}
