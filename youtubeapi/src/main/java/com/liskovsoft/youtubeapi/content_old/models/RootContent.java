package com.liskovsoft.youtubeapi.content_old.models;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class RootContent {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private List<ContentTab> mContentTabs;

    public List<ContentTab> getContentTabs() {
        return mContentTabs;
    }
}
