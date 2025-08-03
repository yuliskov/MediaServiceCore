package com.liskovsoft.youtubeapi.browse.v1.models.guide;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class GuideItem {
    @JsonPath("$.title")
    private String mTitle;

    @JsonPath("$.formattedTitle.simpleText")
    private String mTitleAlt;

    @JsonPath("$.icon.iconType")
    private String mIconType;

    @JsonPath("$.navigationEndpoint.browseEndpoint.browseId")
    private String mBrowseId;

    @JsonPath("$.navigationEndpoint.browseEndpoint.params")
    private String mParams;

    public String getTitle() {
        return mTitle;
    }

    public String getTitleAlt() {
        return mTitleAlt;
    }

    public String getIconType() {
        return mIconType;
    }

    public String getBrowseId() {
        return mBrowseId;
    }

    public String getParams() {
        return mParams;
    }
}
