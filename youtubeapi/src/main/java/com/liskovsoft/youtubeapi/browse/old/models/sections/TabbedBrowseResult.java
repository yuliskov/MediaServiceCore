package com.liskovsoft.youtubeapi.browse.old.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class TabbedBrowseResult {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<BrowseTab> mBrowseTabs;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public List<BrowseTab> getBrowseTabs() {
        return mBrowseTabs;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
