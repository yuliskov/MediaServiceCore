package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.V2.TextItem;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

/**
 * Root element: gridVideoRenderer/pivotVideoRenderer
 */
public class VideoItem {
    private static final String BADGE_STYLE_LIVE = "LIVE";
    private static final String BADGE_STYLE_UPCOMING = "UPCOMING";
    private static final String BADGE_STYLE_DEFAULT = "DEFAULT";
    private static final String BADGE_STYLE_MOVIE = "BADGE_STYLE_TYPE_YPC";
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.navigationEndpoint.watchEndpoint.playlistId")
    private String mPlaylistId;
    @JsonPath("$.navigationEndpoint.watchEndpoint.index")
    private int mPlaylistIndex = -1;
    @JsonPath("$.navigationEndpoint.clickTrackingParams")
    private String mClickTrackingParams;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.channelThumbnail.thumbnails[0]")
    private String mChannelThumbnail;
    @JsonPath("$.title")
    private TextItem mTitle;
    @JsonPath({"$.shortBylineText", "$.shortBylineText", "$.longBylineText"})
    private TextItem mUserName;
    @JsonPath({"$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId",
               "$.longBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId"})
    private String mChannelId;
    @JsonPath("$.menu.menuRenderer.items[*].menuNavigationItemRenderer.navigationEndpoint.browseEndpoint.browseId")
    private List<String> mMenuChannelId;
    @JsonPath("$.menu.menuRenderer.items[*].menuServiceItemRenderer.serviceEndpoint.feedbackEndpoint.feedbackToken")
    private List<String> mFeedbackToken;
    @JsonPath({"$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl",
               "$.longBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl"})
    private String mCanonicalChannelUrl;
    @JsonPath({"$.publishedTimeText.simpleText", "$.publishedTimeText.runs[0].text"})
    private String mPublishedTime;
    @JsonPath("$.viewCountText")
    private TextItem mViewCountText;
    @JsonPath("$.shortViewCountText")
    private TextItem mShortViewCountText;
    @JsonPath("$.lengthText")
    private TextItem mLengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String mLengthTextLong;
    @JsonPath({"$.thumbnailOverlays[0].thumbnailOverlayTimeStatusRenderer.text",
               "$.badges[0].liveBadge.label",
               "$.badges[0].upcomingEventBadge.label"})
    private TextItem mBadgeText;
    @JsonPath("$.badges[0].metadataBadgeRenderer.label")
    private String mDescBadgeText;
    // Sometimes live video contains percent watched as first item
    @JsonPath({"$.thumbnailOverlays[0].thumbnailOverlayTimeStatusRenderer.style",
               "$.thumbnailOverlays[1].thumbnailOverlayTimeStatusRenderer.style"})
    private String mBadgeStyle;
    @JsonPath("$.trackingParams")
    private String mTrackingParams;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched = -1;
    @JsonPath("$.navigationEndpoint.watchEndpoint.startTimeSeconds")
    private int mStartTimeSeconds;
    @JsonPath("$.upcomingEventData.upcomingEventText")
    private TextItem mUpcomingEventText;
    @JsonPath("$.upcomingEventData.startTime")
    private String mUpcomingEventStartTime;
    @JsonPath("$.richThumbnail.movingThumbnailRenderer.movingThumbnailDetails.thumbnails[0].url")
    private String mRichThumbnailUrl;

    public String getVideoId() {
        return mVideoId;
    }

    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getChannelThumbnail() {
        return mChannelThumbnail;
    }

    public String getTitle() {
        return mTitle != null ? Helpers.toString(mTitle.getText()) : null;
    }

    public CharSequence getUserName() {
        return mUserName != null ? mUserName.getText() : null;
    }

    public String getChannelId() {
        return mChannelId != null ? mChannelId : mMenuChannelId != null ? mMenuChannelId.get(0) : null;
    }

    public String getCanonicalChannelUrl() {
        return mCanonicalChannelUrl;
    }

    public String getPublishedDate() {
        return mPublishedTime;
    }

    public CharSequence getViewCountText() {
        return mViewCountText != null ? mViewCountText.getText() : null;
    }

    public CharSequence getShortViewCountText() {
        return mShortViewCountText != null ? mShortViewCountText.getText() : null;
    }

    public String getLengthText() {
        return mLengthText != null ? Helpers.toString(mLengthText.getText()) : null;
    }

    public String getLengthTextLong() {
        return mLengthTextLong;
    }

    public String getBadgeStyle() {
        return mBadgeStyle;
    }

    public boolean isLive() {
        return BADGE_STYLE_LIVE.equals(mBadgeStyle);
    }

    public boolean isUpcoming() {
        return BADGE_STYLE_UPCOMING.equals(mBadgeStyle);
    }

    public boolean isMovie() {
        return BADGE_STYLE_MOVIE.equals(mBadgeStyle);
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public int getStartTimeSeconds() {
        return mStartTimeSeconds;
    }

    public String getTrackingParams() {
        return mTrackingParams;
    }

    public String getBadgeText() {
        return mBadgeText != null ? Helpers.toString(mBadgeText.getText()) : null;
    }

    /**
     * Mostly it's a 4K label
     */
    public String getDescBadgeText() {
        return mDescBadgeText;
    }

    /**
     * Example: Premieres 10/8/20, 1:00 AM
     */
    public CharSequence getUpcomingEventText() {
        return mUpcomingEventText != null ? mUpcomingEventText.getText() : null;
    }

    /**
     * Event start time in unix format: 1602108000
     */
    public String getUpcomingEventStartTime() {
        return mUpcomingEventStartTime;
    }

    /**
     * Animated thumbnail preview in webp format
     */
    public String getRichThumbnailUrl() {
        return mRichThumbnailUrl;
    }

    public String getFeedbackToken() {
        return mFeedbackToken != null ? mFeedbackToken.get(0) : null;
    }

    public String getClickTrackingParams() {
        return mClickTrackingParams;
    }
}
