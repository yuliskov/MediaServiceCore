package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

public class MusicItem {
    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.navigationEndpoint.watchEndpoint.playlistId")
    private String mPlaylistId;
    @JsonPath("$.navigationEndpoint.watchEndpoint.index")
    private int mPlaylistIndex = -1;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.primaryText.simpleText", "$.primaryText.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.secondaryText.simpleText", "$.secondaryText.runs[0].text"})
    private String mUserName;
    @JsonPath("$.tertiaryText.simpleText")
    private String mViewsAndPublished;
    @JsonPath("$.tertiaryText.runs[0].text")
    private String mViewCountText;
    @JsonPath("$.tertiaryText.runs[2].text")
    private String mPublishedText;
    @JsonPath({"$.lengthText.simpleText", "$.lengthText.runs[0].text"})
    private String mLengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String mAccessibilityLength;
    @JsonPath("$.menu.menuRenderer.items[0].menuNavigationItemRenderer.navigationEndpoint.browseEndpoint.browseId")
    private String mChannelId;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched;
    @JsonPath("$.richThumbnail.movingThumbnailRenderer.movingThumbnailDetails.thumbnails[0].url")
    private String mRichThumbnailUrl;

    public String getVideoId() {
        return mVideoId;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public int getPlaylistIndex() {
        return mPlaylistIndex;
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

    public String getViewsAndPublished() {
        return mViewsAndPublished;
    }

    public String getViewCountText() {
        if (mViewCountText != null) {
            return mViewCountText;
        }

        if (mViewsAndPublished == null || !mViewsAndPublished.contains(YouTubeHelper.TEXT_DELIM)) {
            return null;
        }

        return mViewsAndPublished.split(YouTubeHelper.TEXT_DELIM)[0];
    }

    public String getPublishedText() {
        if (mPublishedText != null) {
            return mPublishedText;
        }

        if (mViewsAndPublished == null || !mViewsAndPublished.contains(YouTubeHelper.TEXT_DELIM)) {
            return null;
        }

        return mViewsAndPublished.split(YouTubeHelper.TEXT_DELIM)[1];
    }

    public String getLengthText() {
        return mLengthText;
    }

    public String getAccessibilityLength() {
        return mAccessibilityLength;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public String getRichThumbnailUrl() {
        return mRichThumbnailUrl;
    }
}
