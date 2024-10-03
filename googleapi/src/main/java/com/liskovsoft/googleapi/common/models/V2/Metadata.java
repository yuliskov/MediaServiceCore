package com.liskovsoft.googleapi.common.models.V2;

import com.liskovsoft.googleapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googleapi.common.helpers.ServiceHelper;
import com.liskovsoft.googleapi.common.helpers.YouTubeHelper;
import com.liskovsoft.sharedutils.helpers.Helpers;

import java.util.List;

public class Metadata {
    @JsonPath("$.title")
    private TextItem mTitle;
    @JsonPath("$.lines[0].lineRenderer.items[*].lineItemRenderer.text")
    private List<TextItem> mViewsAndDateText1;
    @JsonPath("$.lines[1].lineRenderer.items[*].lineItemRenderer.text")
    private List<TextItem> mViewsAndDateText2;
    @JsonPath("$.lines[*].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.style")
    private List<String> mBadgeStyles;
    @JsonPath("$.lines[*].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.label")
    private List<String> mBadgeLabels;

    public String getTitle() {
        return mTitle != null ? mTitle.getText() : null;
    }

    public String getUserName() {
        return null; // no user name, just generic lines
    }

    public String getViewCountText() {
        return YouTubeHelper.createInfo(getViewCountText1(), getViewCountText2());
    }

    public String getPublishedTime() {
        return null; // should be null (views and dates is combined)
    }

    public List<String> getBadgeStyles() {
        return mBadgeStyles;
    }

    public List<String> getBadgeLabels() {
        return mBadgeLabels;
    }

    private String getViewCountText1() {
        return mViewsAndDateText1 != null ? Helpers.combineItems(" ", mViewsAndDateText1.toArray(new Object[0])) : null;
    }

    private String getViewCountText2() {
        return mViewsAndDateText2 != null ? Helpers.combineItems(" ", mViewsAndDateText2.toArray(new Object[0])) : null;
    }
}
