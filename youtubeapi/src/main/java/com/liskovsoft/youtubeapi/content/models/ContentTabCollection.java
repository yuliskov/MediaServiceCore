package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

@JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*].tabRenderer")
public class ContentTabCollection extends JsonPathCollection<ContentTab> {
    public ContentTabCollection() {
        super(ContentTab.class);
    }
}
