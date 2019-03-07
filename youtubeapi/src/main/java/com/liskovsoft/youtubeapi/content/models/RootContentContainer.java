package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPathCollection;

public class RootContentContainer {
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
    private JsonPathCollection<ContentTab> mContentTabs = new JsonPathCollection<>(ContentTab.class);

    public JsonPathCollection<ContentTab> getContentTabs() {
        return mContentTabs;
    }
}
