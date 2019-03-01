package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

import java.util.List;

public class ContentTab {
    @JsonPath("$.tabRenderer.title")
    private String title;
    @JsonPath("$.tabRenderer.endpoint.browseEndpoint.browseId")
    private String browseId;
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.tabRenderer.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*]")
    private JsonPathCollection<ContentTabSection> sections = new JsonPathCollection<>(ContentTabSection.class); // type erase fix
}
