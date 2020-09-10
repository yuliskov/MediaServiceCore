package com.liskovsoft.youtubeapi.browse.ver2.models.rows;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class RowsTabResult {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<RowsTab> mTabs;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public List<RowsTab> getTabs() {
        return mTabs;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
