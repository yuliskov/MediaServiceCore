package com.liskovsoft.youtubeapi.search.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class TitleItem {
    private static final String BADGE_STYLE_LIVE = "LIVE";
    private static final String BADGE_STYLE_UPCOMING = "UPCOMING";
    private static final String BADGE_STYLE_DEFAULT = "DEFAULT";

    @JsonPath("$.header.tileHeaderRenderer")
    private Header mHeader;

    @JsonPath("$.metadata.tileMetadataRenderer")
    private Metadata mMetadata;

    @JsonPath("$.onSelectCommand.watchEndpoint.videoId")
    private String mVideoId;

    @JsonPath("$.onSelectCommand.browseEndpoint.browseId")
    private String mBrowseId;

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
        return null;
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
        return mHeader != null ? mHeader.getPercentWatched() : 0;
    }

    public String getRichThumbnailUrl() {
        return null;
    }

    public String getChannelId() {
        return mBrowseId;
    }

    public int getPlaylistIndex() {
        return 0;
    }

    public String getPlaylistId() {
        return null;
    }

    public boolean isLive() {
        return BADGE_STYLE_LIVE.equals(mHeader != null ? mHeader.getBadgeStyle() : mMetadata.getBadgeStyle() != null ? mMetadata.getBadgeStyle() : null);
    }

    public boolean isUpcoming() {
        return BADGE_STYLE_UPCOMING.equals(mHeader != null ? mHeader.getBadgeStyle() : mMetadata.getBadgeStyle() != null ? mMetadata.getBadgeStyle() : null);
    }

    public String getFeedbackToken() {
        return null;
    }

    public String getClickTrackingParams() {
        return null;
    }
}
