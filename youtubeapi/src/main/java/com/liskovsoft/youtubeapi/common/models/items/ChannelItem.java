package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

// root element: pivotChannelRenderer
public class ChannelItem {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text", "$.displayName.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.channelId", "$.navigationEndpoint.browseEndpoint.browseId"})
    private String mChannelId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String mVideoCount1;
    @JsonPath("$.videoCountText.runs[1].text")
    private String mVideoCount2;
    @JsonPath({"$.subscriberCountText.simpleText", "$.subscriberCountText.runs[0].text"})
    private String mSubscriberCountText;

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public String getVideoCountText() {
        String result;

        if (mVideoCount2 != null) {
            result = mVideoCount1 + mVideoCount2;
        } else {
            result = mVideoCount1;
        }

        return result;
    }

    public String getSubscriberCountText() {
        return mSubscriberCountText;
    }
}
