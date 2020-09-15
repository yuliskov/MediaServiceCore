package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class VideoItem {
    private static final String THUMBNAIL_LIVE = "LIVE";
    private static final String THUMBNAIL_STYLE_DEFAULT = "DEFAULT";
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.channelThumbnail.thumbnails[0]")
    private String mChannelThumbnail;
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.shortBylineText.runs[0].text", "$.longBylineText.runs[0].text"})
    private String mUserName;
    @JsonPath({"$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId",
               "$.longBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId",
               "$.menu.menuRenderer.items[0].menuNavigationItemRenderer.navigationEndpoint.browseEndpoint.browseId"})
    private String mChannelId;
    @JsonPath({"$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl",
               "$.longBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl"})
    private String mCanonicalChannelUrl;
    @JsonPath({"$.publishedTimeText.simpleText", "$.publishedTimeText.runs[0].text"})
    private String mPublishedTime;
    @JsonPath({"$.viewCountText.simpleText", "$.viewCountText.runs[0].text"})
    private String mViewCountText;
    @JsonPath({"$.shortViewCountText.simpleText", "$.shortViewCountText.runs[0].text"})
    private String mShortViewCount;
    @JsonPath({"$.lengthText.simpleText", "$.lengthText.runs[0].text"})
    private String mLengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String mAccessibilityLengthText;
    @JsonPath("$.badges[0].textBadge.label.runs[0].text")
    private String mQualityBadge;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayTimeStatusRenderer.style")
    private String mThumbnailStyle;
    @JsonPath("$.badges[0].liveBadge.label.runs[0].text")
    private String mLiveBadge;
    @JsonPath("$.trackingParams")
    private String mTrackingParams;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched;

    public String getVideoId() {
        return mVideoId;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getChannelThumbnail() {
        return mChannelThumbnail;
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

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public String getViewCountText() {
        return mViewCountText;
    }

    public String getShortViewCount() {
        return mShortViewCount;
    }

    public String getLengthText() {
        return mLengthText;
    }

    public String getAccessibilityLength() {
        return mAccessibilityLengthText;
    }

    public String getQualityBadge() {
        return mQualityBadge;
    }

    public String getThumbnailStyle() {
        return mThumbnailStyle;
    }

    public boolean isLive() {
        return THUMBNAIL_LIVE.equals(mLiveBadge);
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public String getTrackingParams() {
        return mTrackingParams;
    }

    public String getLiveBadge() {
        return mLiveBadge;
    }
}
