package com.liskovsoft.youtubeapi.browse.v1.models.grid;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.Collections;
import java.util.List;

public class GridTabList {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<GridTab> mTabs;

    @JsonPath("$.contents.tvBrowseRenderer.content.tvSurfaceContentRenderer.content.sectionListRenderer")
    private GridTab mHistoryTab;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public List<GridTab> getTabs() {
        return mTabs != null ? mTabs : Collections.singletonList(mHistoryTab);
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
