package com.liskovsoft.youtubeapi.content;

import com.liskovsoft.youtubeapi.support.JsonPath;

import java.util.List;

@JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs")
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
