package com.liskovsoft.youtubeapi.common.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class TileItem {
    public static final String CONTENT_TYPE_UNDEFINED = "UNDEFINED";
    public static final String CONTENT_TYPE_CHANNEL = "TILE_CONTENT_TYPE_CHANNEL";
    public static final String CONTENT_TYPE_PLAYLIST = "TILE_CONTENT_TYPE_PLAYLIST";
    public static final String CONTENT_TYPE_VIDEO = "TILE_CONTENT_TYPE_VIDEO";
    private static final String BADGE_STYLE_LIVE = "LIVE";
    private static final String BADGE_STYLE_UPCOMING = "UPCOMING";
    private static final String BADGE_STYLE_DEFAULT = "DEFAULT";

    @JsonPath("$.header.tileHeaderRenderer")
    private Header mHeader;

    @JsonPath("$.metadata.tileMetadataRenderer")
    private Metadata mMetadata;

    @JsonPath("$.onSelectCommand.watchEndpoint.videoId")
    private String mVideoId;

    @JsonPath({
            "$.onSelectCommand.watchEndpoint.playlistId",
            "$.onSelectCommand.watchPlaylistEndpoint.playlistId"
    })
    private String mPlaylistId;

    @JsonPath("$.onSelectCommand.browseEndpoint.params")
    private String mPlaylistParams;

    @JsonPath("$.onSelectCommand.browseEndpoint.browseId")
    private String mChannelId;

    @JsonPath("$.contentType")
    private String mContentType;

    @JsonPath("$.menu.menuRenderer.items[*].menuServiceItemRenderer.serviceEndpoint.feedbackEndpoint.feedbackToken")
    private List<String> mFeedbackToken;

    public Header getHeader() {
        return mHeader;
    }

    public Metadata getMetadata() {
        return mMetadata;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getTitle() {
        return mMetadata != null ? mMetadata.getTitle() : null;
    }

    public String getDescBadgeText() {
        return mMetadata != null ? mMetadata.getDescBadgeText() : null;
    }

    public String getUserName() {
        return mMetadata != null ? mMetadata.getUserName() : null;
    }

    public String getPublishedTime() {
        return mMetadata != null ? mMetadata.getPublishedTime() : null;
    }

    public String getViewCountText() {
        return mMetadata != null ? mMetadata.getViewCountText() : null;
    }

    public String getUpcomingEventText() {
        return null;
    }

    public List<Thumbnail> getThumbnails() {
        return mHeader != null ? mHeader.getThumbnails() : null;
    }

    public String getLengthText() {
        return mHeader != null ? mHeader.getDuration() : null;
    }

    public String getBadgeText() {
        return mHeader != null ? mHeader.getBadgeText() : null;
    }

    public int getPercentWatched() {
        return mHeader != null ? mHeader.getPercentWatched() : -1;
    }

    public String getRichThumbnailUrl() {
        return mHeader != null ? mHeader.getMovingThumbnailUrl() : null;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public int getPlaylistIndex() {
        return 0;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public String getPlaylistParams() {
        return mPlaylistParams;
    }

    public boolean isLive() {
        return BADGE_STYLE_LIVE.equals(mHeader != null ? mHeader.getBadgeStyle() : mMetadata.getBadgeStyle() != null ? mMetadata.getBadgeStyle() : null);
    }

    public boolean isUpcoming() {
        return BADGE_STYLE_UPCOMING.equals(mHeader != null ? mHeader.getBadgeStyle() : mMetadata.getBadgeStyle() != null ? mMetadata.getBadgeStyle() : null);
    }

    public String getFeedbackToken() {
        return mFeedbackToken != null ? mFeedbackToken.get(0) : null;
    }

    public String getClickTrackingParams() {
        return null;
    }

    public String getContentType() {
        return mContentType != null ? mContentType : CONTENT_TYPE_UNDEFINED;
    }
}
