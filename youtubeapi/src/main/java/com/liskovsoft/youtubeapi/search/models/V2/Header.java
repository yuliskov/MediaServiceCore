package com.liskovsoft.youtubeapi.search.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class Header {
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayTimeStatusRenderer.text.simpleText")
    private String mDuration;

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getDuration() {
        return mDuration;
    }
}
