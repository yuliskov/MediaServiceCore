package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;

import java.util.ArrayList;

@JsonPath("$.contents.tvBrowseRenderer.content.tvSecondaryNavRenderer.sections[0].tvSecondaryNavSectionRenderer.tabs[*]")
public class ContentTabCollection extends ArrayList<ContentTab> {
}
