package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;

import java.util.List;

/**
 * Root element: gridVideoRenderer/pivotVideoRenderer
 */
public class VideoItem {
    private static final String BADGE_TEXT_LIVE = "LIVE";
    private static final String BADGE_STYLE_LIVE = "LIVE";
    private static final String BADGE_STYLE_UPCOMING = "UPCOMING";
    private static final String BADGE_STYLE_DEFAULT = "DEFAULT";
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
    private String mShortViewCountText1;
    @JsonPath("$.shortViewCountText.runs[1].text")
    private String mShortViewCountText2;
    @JsonPath({"$.lengthText.simpleText", "$.lengthText.runs[0].text"})
    private String mLengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String mLengthTextLong;
    @JsonPath({"$.badges[0].liveBadge.label.runs[0].text", "$.badges[0].upcomingEventBadge.label.simpleText"})
    private String mBadgeText;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayTimeStatusRenderer.style")
    private String mBadgeStyle;
    @JsonPath("$.trackingParams")
    private String mTrackingParams;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched;
    @JsonPath("$.upcomingEventData.upcomingEventText.runs[0].text")
    private String mUpcomingEventText1;
    @JsonPath("$.upcomingEventData.upcomingEventText.runs[1].text")
    private String mUpcomingEventText2;
    @JsonPath("$.upcomingEventData.startTime")
    private String mUpcomingEventStartTime;

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

    public String getShortViewCountText() {
        return AppHelper.combineText(mShortViewCountText1, mShortViewCountText2);
    }

    public String getLengthText() {
        return mLengthText;
    }

    public String getLengthTextLong() {
        return mLengthTextLong;
    }

    public String getBadgeStyle() {
        return mBadgeStyle;
    }

    public boolean isLive() {
        return BADGE_STYLE_LIVE.equals(mBadgeStyle) || BADGE_TEXT_LIVE.equals(mBadgeText);
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public String getTrackingParams() {
        return mTrackingParams;
    }

    public String getBadgeText() {
        return mBadgeText;
    }

    /**
     * Example: Premieres 10/8/20, 1:00 AM
     */
    public String getUpcomingEventText() {
        return AppHelper.combineText(mUpcomingEventText1, mUpcomingEventText2);
    }

    /**
     * Event start time in unix format: 1602108000
     */
    public String getUpcomingEventStartTime() {
        return mUpcomingEventStartTime;
    }
}
