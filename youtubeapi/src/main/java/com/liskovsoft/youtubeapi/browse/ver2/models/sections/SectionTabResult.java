package com.liskovsoft.youtubeapi.browse.ver2.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SectionTabResult {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<SectionTab> mTabs;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public List<SectionTab> getTabs() {
        return mTabs;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}
