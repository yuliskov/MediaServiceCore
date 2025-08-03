package com.liskovsoft.youtubeapi.next.v1.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

public class VideoOwner {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mVideoAuthor;
    @JsonPath({"$.subscriptionButton.subscribed", "$.subscribed", "$.subscribeButton.subscribeButtonRenderer.subscribed"})
    private Boolean mIsSubscribed;
    @JsonPath({"$.navigationEndpoint.browseEndpoint.browseId", "$.subscribeButton.subscribeButtonRenderer.channelId"})
    private String mChannelId;
    @JsonPath({"$.subscriberCountText.simpleText", "$.subscriberCountText.runs[0].text", "$.subscribeButton.subscribeButtonRenderer.subscriberCountText.runs[0].text"})
    private String mSubscriberCount;
    @JsonPath({"$.longSubscriberCountText.runs[0].text", "$.subscribeButton.subscribeButtonRenderer.longSubscriberCountText.runs[0].text"})
    private String mSubscriberCountLong;
    @JsonPath({"$.shortSubscriberCountText.runs[0].text", "$.subscribeButton.subscribeButtonRenderer.shortSubscriberCountText.runs[0].text"})
    private String mSubscriberCountShort;

    public String getVideoAuthor() {
        return mVideoAuthor;
    }

    public Boolean isSubscribed() {
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

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }
}
