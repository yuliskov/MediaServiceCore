package com.liskovsoft.youtubeapi.common.models.V2;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.V2.TextItem;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;
import com.liskovsoft.sharedutils.helpers.Helpers;

import java.util.List;

public class Header {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.thumbnailOverlays[*].thumbnailOverlayTimeStatusRenderer.text")
    private List<TextItem> mBadgeText;
    @JsonPath("$.thumbnailOverlays[*].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private List<Integer> mPercentWatched;
    @JsonPath("$.thumbnailOverlays[*].thumbnailOverlayTimeStatusRenderer.style")
    private List<String> mBadgeStyle;
    @JsonPath({
            "$.onFocusThumbnail.thumbnails[0].url", // v2
            "$.movingThumbnail.thumbnails[0].url" // v1
    })
    private String mMovingThumbnailUrl;
    @JsonPath("$.title")
    private TextItem mTitle;

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getBadgeText() {
        return mBadgeText != null ? Helpers.toString(mBadgeText.get(0).getText()) : null;
    }

    public int getPercentWatched() {
        return mPercentWatched != null ? mPercentWatched.get(0) : -1;
    }

    public String getBadgeStyle() {
        return mBadgeStyle != null ? mBadgeStyle.get(0) : null;
    }

    /**
     * Animated thumbnail preview in webp format
     */
    public String getMovingThumbnailUrl() {
        return mMovingThumbnailUrl;
    }

    public String getTitle() {
        return mTitle != null ? Helpers.toString(mTitle.getText()) : null;
    }
}
