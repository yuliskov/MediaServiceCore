package com.liskovsoft.youtubeapi.search.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class Header {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayTimeStatusRenderer.text.simpleText")
    private String mDuration;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched;
    @JsonPath("$.thumbnailOverlays[1].thumbnailOverlayTimeStatusRenderer.style")
    private String mBadgeStyle;
    @JsonPath("$.thumbnailOverlays[1].thumbnailOverlayTimeStatusRenderer.text")
    private TextItem mBadgeText;

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getDuration() {
        return mDuration;
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public String getBadgeStyle() {
        return mBadgeStyle;
    }

    public String getBadgeText() {
        return mBadgeText != null ? mBadgeText.getText() : null;
    }
}
