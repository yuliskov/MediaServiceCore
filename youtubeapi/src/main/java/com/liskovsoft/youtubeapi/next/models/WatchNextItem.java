package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;

import java.util.List;

public class WatchNextItem {
    private static final String BADGE_STYLE_LIVE = "LIVE";
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.title.runs[0].text", "$.title.simpleText"})
    private String mTitle;
    @JsonPath("$.shortBylineText.runs[0].text")
    private String mUserName;
    @JsonPath("$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId")
    private String mChannelId;
    @JsonPath("$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl")
    private String mCanonicalChannelUrl;
    @JsonPath({"$.viewCountText.runs[0].text", "$.viewCountText.simpleText"})
    private String mViewCountText;
    @JsonPath({"$.lengthText.runs[0].text", "$.lengthText.simpleText"})
    private String mLengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String mAccessibilityLengthText;
    @JsonPath("$.trackingParams")
    private String mTrackingParams;
    @JsonPath("$.badges[0].liveBadge.label.runs[0].text")
    private String mBadgeStyle;

    public String getVideoId() {
        return mVideoId;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public String getCanonicalChannelUrl() {
        return mCanonicalChannelUrl;
    }

    public String getViewCountText() {
        return mViewCountText;
    }

    public String getLengthText() {
        return mLengthText;
    }

    public String getAccessibilityLength() {
        return mAccessibilityLengthText;
    }

    public String getTrackingParams() {
        return mTrackingParams;
    }

    public String getBadgeStyle() {
        return mBadgeStyle;
    }

    public boolean isLive() {
        return BADGE_STYLE_LIVE.equals(mBadgeStyle);
    }
}
