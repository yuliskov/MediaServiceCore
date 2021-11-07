package com.liskovsoft.youtubeapi.browse.models.grid;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class GridTabList {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<GridTab> mTabs;

    @JsonPath("$.contents.tvBrowseRenderer.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*]")
    private List<GridTab> mHistoryTabs;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public List<GridTab> getTabs() {
        return mTabs != null ? mTabs : mHistoryTabs;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
