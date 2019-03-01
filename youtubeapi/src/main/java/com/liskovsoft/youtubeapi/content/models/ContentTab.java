package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;

import java.util.List;

@JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*]")
public class ContentTab {
    @JsonPath("$.tabRenderer.title")
    private String title;
    @JsonPath("$.tabRenderer.endpoint.browseEndpoint.browseId")
    private String browseId;
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.tabRenderer.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents")
    private List<ContentTabSection> sections;
}
