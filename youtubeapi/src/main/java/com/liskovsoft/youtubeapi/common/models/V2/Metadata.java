package com.liskovsoft.youtubeapi.common.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

import java.util.List;

public class Metadata {
    @JsonPath("$.title")
    private TextItem mTitle;
    @JsonPath("$.lines[0].lineRenderer.items[*].lineItemRenderer.text")
    private List<TextItem> mViewsAndDateText1;
    @JsonPath("$.lines[1].lineRenderer.items[*].lineItemRenderer.text")
    private List<TextItem> mViewsAndDateText2;
    @JsonPath({"$.lines[0].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.style",
            "$.lines[1].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.style"})
    private String mBadgeStyle;
    @JsonPath({"$.lines[0].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.label",
            "$.lines[1].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.label"})
    private String mDescBadgeText;

    public String getTitle() {
        return mTitle != null ? mTitle.getText() : null;
    }

    public String getUserName() {
        return null; // no user name, just generic lines
    }

    public String getViewCountText() {
        return YouTubeMediaServiceHelper.createDescription(getViewCountText1(), getViewCountText2());
    }

    public String getPublishedTime() {
        return null; // should be null (views and dates is combined)
    }

    public String getBadgeStyle() {
        return mBadgeStyle;
    }

    public String getDescBadgeText() {
        return mDescBadgeText;
    }

    private String getViewCountText1() {
        return mViewsAndDateText1 != null ? ServiceHelper.combineItems(" ", mViewsAndDateText1.toArray(new Object[0])) : null;
    }

    private String getViewCountText2() {
        return mViewsAndDateText2 != null ? ServiceHelper.combineItems(" ", mViewsAndDateText2.toArray(new Object[0])) : null;
    }
}
