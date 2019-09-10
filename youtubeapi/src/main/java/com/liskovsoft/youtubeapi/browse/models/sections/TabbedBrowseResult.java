package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class TabbedBrowseResult {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<BrowseTab> mBrowseTabs;

    public List<BrowseTab> getBrowseTabs() {
        return mBrowseTabs;
    }
}
