package com.liskovsoft.youtubeapi.common.models.videos;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class MusicItem {
    private static final String TERTIARY_TEXT_DELIM = "•";
    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.primaryText.simpleText", "$.primaryText.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.secondaryText.simpleText", "$.secondaryText.runs[0].text"})
    private String mUserName;
    @JsonPath("$.navigationEndpoint.watchEndpoint.playlistId")
    private String mPlaylistId;
    @JsonPath("$.tertiaryText.simpleText")
    private String mViewsAndPublished;
    @JsonPath({"$.lengthText.simpleText", "$.lengthText.runs[0].text"})
    private String mLengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String mAccessibilityLength;
    @JsonPath("$.menu.menuRenderer.items[0].menuNavigationItemRenderer.navigationEndpoint.browseEndpoint.browseId")
    private String mChannelId;

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

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public String getViewsAndPublished() {
        return mViewsAndPublished;
    }

    public String getViewCount() {
        if (mViewsAndPublished == null || !mViewsAndPublished.contains(TERTIARY_TEXT_DELIM)) {
            return null;
        }

        return mViewsAndPublished.split(TERTIARY_TEXT_DELIM)[0];
    }

    public String getPublishedTime() {
        if (mViewsAndPublished == null || !mViewsAndPublished.contains(TERTIARY_TEXT_DELIM)) {
            return null;
        }

        return mViewsAndPublished.split(TERTIARY_TEXT_DELIM)[1];
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
}
