package com.liskovsoft.youtubeapi.common.models.V2;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

import java.util.List;

public class Metadata {
    @JsonPath("$.title")
    private TextItem mTitle;
    @JsonPath("$.lines[0].lineRenderer.items[0].lineItemRenderer.text")
    private TextItem mUserName;
    @JsonPath("$.lines[1].lineRenderer.items[*].lineItemRenderer.text")
    private List<TextItem> mViewsAndDateText;
    @JsonPath("$.lines[1].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.style")
    private String mBadgeStyle;
    @JsonPath("$.lines[1].lineRenderer.items[0].lineItemRenderer.badge.metadataBadgeRenderer.label")
    private String mDescBadgeText;

    public String getTitle() {
        return mTitle != null ? mTitle.getText() : null;
    }

    public String getUserName() {
        return mUserName != null ? mUserName.getText() : null;
    }

    public String getViewCountText() {
        return mViewsAndDateText != null ? ServiceHelper.combineItems(mViewsAndDateText.toArray(new Object[0]), " ") : null;
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
}
